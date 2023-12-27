package com.grandtheftwarzone.core.emoji.gui;

import com.google.common.collect.Queues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.commons.codec.digest.DigestUtils;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.helper.Helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.grandtheftwarzone.core.emoji.GTWEmoji.minecraftDir;


/**
 * A temporary gui for exporting icons.
 *
 * For each tick it is opened, it will render one icon, take a screenshot, and write it to a file.
 *
 * @author rubensworks
 */
public class GuiIconExporter extends GuiScreen {

    private static final int BACKGROUND_COLOR = Helpers.RGBAToInt(1, 0, 0, 255);
    private final List<ItemInfo> itemInfoList;

    private final int scale;
    private final Queue<IExportTask> exportTasks;



    public GuiIconExporter(int scale) {
        this.scale = scale;
        this.exportTasks = this.createExportTasks();
        this.itemInfoList = new ArrayList<>();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (exportTasks.isEmpty()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            generateItemsYml();
        } else {
            IExportTask task = exportTasks.poll();
            try {
                task.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateItemsYml() {
        try {

            File baseDir = new File(minecraftDir, "gtwdata/exportItem" + (this.scale * 2) + "/");
            Path itemsYmlPath = baseDir.toPath().resolve("items.yml");

            try (BufferedWriter writer = Files.newBufferedWriter(itemsYmlPath)) {
                for (ItemInfo itemInfo : itemInfoList) {
                    writer.write("- name: " + itemInfo.getName());
                    writer.newLine();
                    writer.write("  location: \'" + itemInfo.getLocation() + "\'");
                    writer.newLine();
                    writer.write("  strings:");
                    writer.newLine();
                    for (String string : itemInfo.getStrings()) {
                        writer.write("  - '" + string + "'");
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ItemInfo {
        private final String name;
        private final String location;
        private final List<String> strings;

        public ItemInfo(String name, String location, List<String> strings) {
            this.name = name.replace(":", "__").replace("\"", "@");
            this.location = location.replace(":", "__").replace("\"", "@");
            this.strings = replaceColonInStrings(strings);
        }

        private List<String> replaceColonInStrings(List<String> strings) {
            for (int i = 0; i < strings.size(); i++) {
                strings.set(i, strings.get(i).replace(":", "__"));
                strings.set(i, strings.get(i).replace("\"", "@"));
            }
            return strings;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public List<String> getStrings() {
            return strings;
        }
    }

    public String serializeNbtTag(NBTTagCompound tag) {
        if (GeneralConfig.fileNameHashTag) {
            return DigestUtils.md5Hex(tag.toString());
        } else {
            return tag.toString();
        }
    }

    public Queue<IExportTask> createExportTasks() {
        File newDir = new File(minecraftDir, "gtwdata/");
        newDir.mkdir();
        File newDir1 = new File(minecraftDir + "/gtwdata/", "exportItem" + (this.scale * 2));
        newDir1.mkdir();
        File baseDir = new File(minecraftDir + "/gtwdata/exportItem" + (this.scale * 2), "item/");
        baseDir.mkdir();

        // Create a list of tasks
        Wrapper<Integer> tasks = new Wrapper<>(0);
        Wrapper<Integer> taskProcessed = new Wrapper<>(0);
        Queue<IExportTask> exportTasks = Queues.newArrayDeque();

        // Add fluids
        for (Map.Entry<String, Fluid> fluidEntry : FluidRegistry.getRegisteredFluids().entrySet()) {
            tasks.set(tasks.get() + 1);
            String subKey = "fluid__" + fluidEntry.getKey();
            exportTasks.add(() -> {
                taskProcessed.set(taskProcessed.get() + 1);
                drawRect(0, 0, this.scale, this.scale, BACKGROUND_COLOR);
                ItemRenderUtil.renderFluid(this, fluidEntry.getValue(), this.scale);
                ImageExportUtil.exportImageFromScreenshot(baseDir, subKey, this.width, this.height, this.scale, BACKGROUND_COLOR);
                List<String> strings = new ArrayList<>();
                subKey.replace(":", "__");
                subKey.replace("\"", "@");
                strings.add("~" + subKey + "~");
                itemInfoList.add(new ItemInfo(subKey, "assets/item/" + subKey + ".png", strings));
            });
        }

        // Add items
        for (ResourceLocation key : Item.REGISTRY.getKeys()) {
            Item value = Item.REGISTRY.getObject(key);
            NonNullList<ItemStack> subItems = NonNullList.create();
            value.getSubItems(CreativeTabs.SEARCH, subItems);
            for (ItemStack subItem : subItems) {
                tasks.set(tasks.get() + 1);
                String subKey = key + "__" + subItem.getMetadata() + (subItem.hasTagCompound() ? "__" + serializeNbtTag(subItem.getTagCompound()) : "");
                exportTasks.add(() -> {
                    taskProcessed.set(taskProcessed.get() + 1);
                    drawRect(0, 0, this.scale, this.scale, BACKGROUND_COLOR);
                    ItemRenderUtil.renderItem(subItem, this.scale);
                    ImageExportUtil.exportImageFromScreenshot(baseDir, subKey, this.width, this.height, this.scale, BACKGROUND_COLOR);
                    List<String> strings = new ArrayList<>();
                    subKey.replace(":", "__");
                    subKey.replace("\"", "@");
                    strings.add("~" + subKey + "~");
                    itemInfoList.add(new ItemInfo(subKey, "assets/item/" + subKey + ".png", strings));
                    if (subItem.hasTagCompound() && GeneralConfig.fileNameHashTag) {
                        ImageExportUtil.exportNbtFile(baseDir, subKey, subItem.getTagCompound());
                    }
                });
            }
        }

        return exportTasks;
    }


}
