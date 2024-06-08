package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.emoji.RLEmoji;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkerCreationStateMachine {

    private enum State {
        ENTER_NAME,
        ENTER_COORD,
        ENTER_ICON_ID,
        FINISHED,
        STOP
    }

    private State currentState;
    private String name;
    private String coords = null;
    private String iconId;
    private List<RLEmoji> emojiList = null;
    private List<String> emojiListStr = new ArrayList<>();
    private int timer;

    private boolean server;

    public MarkerCreationStateMachine(String coords, boolean server) {
        this.currentState = State.ENTER_NAME;
        MinecraftForge.EVENT_BUS.register(this);
        this.coords = coords;
        this.server = server;
        this.timer = 199;
    }

    public MarkerCreationStateMachine(String coords) {
        this(coords, false);
    }

    public void processInput(String input) {
        Minecraft mc = Minecraft.getMinecraft();
        switch (currentState) {
            case ENTER_NAME:
                if (input.contains(" ")) {
                    TextComponentString textN = new TextComponentString("\n§8[GTWMap] §сThe name cannot contain spaces.");
                    mc.player.sendMessage(textN);
                    return;
                }
                this.name = input;
                TextComponentString textN = new TextComponentString("§8[GTWMap] §aThe name is set.");
                mc.player.sendMessage(textN);
                if (coords == null) {
                    this.currentState = State.ENTER_COORD;
                } else {
                    this.currentState = State.ENTER_ICON_ID;
                }
                timer = 180;
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
                }
                break;
            case ENTER_COORD:
                this.coords = input.replace(" ", ";");
                TextComponentString text = new TextComponentString("\n§8[GTWMap] §aThe coordinates are set.");
                mc.player.sendMessage(text);
                this.currentState = State.ENTER_ICON_ID;
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
                }
                timer = 180;
                break;
            case ENTER_ICON_ID:
                if (!emojiListStr.contains(input)) {
                    TextComponentString textC = new TextComponentString("\n§8[GTWMap] §сThis icon is not in the list!");
                    mc.player.sendMessage(textC);
                    break;
                }
                this.iconId = input;
                TextComponentString textI = new TextComponentString("\n§8[GTWMap] §aThe icon id is set.");
                mc.player.sendMessage(textI);
                this.currentState = State.FINISHED;
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
                }
                timer = 180;
                break;
            case FINISHED:
                break;
        }
    }

    public void stop() {
        if (emojiList != null) {
            for (RLEmoji emoji : emojiList) {
                GtwAPI.getInstance().getEmojiManagerClient().removeRLEmoji(emoji);
            }
        }
        GtwAPI.getInstance().getMapManagerClient().setMarkerCreator(null);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        currentState = State.STOP;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        timer++;
        if (timer >= 200) {
            timer = 0;
            switch (currentState) {
                case ENTER_NAME:
                    sendTextName();
                    break;
                case ENTER_COORD:
                    sendTextCoord();
                    break;
                case ENTER_ICON_ID:
                    sendTextIcon();
                    break;
                case FINISHED:
                    stop();
                    createMarker();
                    break;
                case STOP:
                    break;
            }
        }
    }

    public void createMarker() {
        if (server) {
            EntityLocation location = new EntityLocation(coords);
            String command = "/mapmanager create " + name + " " + iconId + " null " + " " + location.getX() + " " + location.getZ() + " " + location.getY();
            Minecraft.getMinecraft().player.sendChatMessage(command);

        } else {
            List<String> mapImageId = new ArrayList<>();
            mapImageId.add(GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage().getImageId());
            TemplateMarker templateMarker = new TemplateMarker(name, null, iconId, new EntityLocation(coords).toString(), null, true, mapImageId, null, true);

            GtwAPI.getInstance().getMapManagerClient().getMarkerManager().addLocalMarker(new BaseStaticMarker(templateMarker));
            String msg = "§8[LMM] §aMarker §f" + name + "§a successfully created! §7[Show markers]";
            TextComponentString clickableMsg = new TextComponentString(msg);
            Style clickableStyle = new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lmm list"));
            clickableMsg.setStyle(clickableStyle);
            Minecraft.getMinecraft().player.sendMessage(clickableMsg);

        }

        currentState = State.STOP;
    }

    public void sendTextName() {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        }
        String text = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n§7============================================\n\n\n§eEnter marker name §7(Or §f\"cancel\")\n\n\n§7============================================";

        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(text));

    }

    public void sendTextCoord() {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        }
        String text = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n§7============================================\n\n\n§eEnter coordinates. \n\n§7(Or click on the message to submit your position)\n\n\n§7============================================";
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        String argClick = player.posX + " " + player.posY + " " + player.posZ + " " + player.rotationYaw + " " + player.rotationPitch;
        TextComponentString textComponents = getClicableMsg(text, ClickEvent.Action.RUN_COMMAND, argClick, HoverEvent.Action.SHOW_TEXT, "Put my coordinates");
        player.sendMessage(textComponents);
    }

    public void sendTextIcon() {
        if (!GtwAPI.getInstance().getEmojiManagerClient().isRenderEmoji()) {
            GtwAPI.getInstance().getEmojiManagerClient().setRenderEmoji(true);
        }

        if (emojiList == null) {
            initEmoji();
        }

        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        }

        TextComponentString msg = new TextComponentString("\n\n\n\n\n\n\n\n\n§7============================================\n\n\n§eSelect an icon: §7(Click)\n\n");

        for (int i = 0; i < emojiListStr.size(); i++) {
            String iconName = emojiListStr.get(i);
            String formatIconName = ":mM" + iconName + ":";
            if (i % 5 == 0 && i != 0) {
                msg.appendSibling(new TextComponentString("\n\n"));
            }
            TextComponentString icon = getClicableMsg(formatIconName + "  ", ClickEvent.Action.RUN_COMMAND, iconName, HoverEvent.Action.SHOW_TEXT, "§aSet icon: " + formatIconName);
            msg.appendSibling(icon);

        }
        msg.appendSibling(new TextComponentString("\n\n§7============================================"));

        Minecraft.getMinecraft().player.sendMessage(msg);

    }

    public void initEmoji() {
        File gameDir = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getGameDir();
        String markerDirPath = gameDir.getAbsolutePath() + File.separator + "markers";

        emojiList = new ArrayList<>();

        List<String> markerFiles = getFilesInDirectoryWithoutExtension(markerDirPath);

        for (String marker : markerFiles) {
            if (!server && marker.startsWith("SERVER_")) {
                continue;
            }

            ResourceLocation icon = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage("markers/" + marker);
            if (icon == null) {
                continue;
            }
            RLEmoji emoji = new RLEmoji(icon, Arrays.asList(":mM" + marker + ":"));

            emojiListStr.add(marker);
            emojiList.add(emoji);
            GtwAPI.getInstance().getEmojiManagerClient().addRLEmoji(emoji);
        }
    }

    public static List<String> getFilesInDirectoryWithoutExtension(String directoryPath) {
        List<String> fileNames = new ArrayList<>();

        File directory = new File(directoryPath);

        // Проверяем, является ли указанный путь директорией
        if (directory.isDirectory()) {
            // Получаем список всех файлов в директории
            File[] files = directory.listFiles();

            // Проверяем, что список файлов не пустой
            if (files != null) {
                for (File file : files) {
                    // Получаем имя каждого файла, обрезаем расширение и добавляем его в список
                    String fileName = file.getName();
                    int indexOfDot = fileName.lastIndexOf(".");
                    if (indexOfDot != -1) {
                        fileName = fileName.substring(0, indexOfDot);
                    }
                    fileNames.add(fileName);
                }
            }
        }

        return fileNames;
    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, HoverEvent.Action eventHover, String argHover) {
        TextComponentString text = new TextComponentString(textMsg);
        text.setStyle(new Style().setClickEvent(new ClickEvent(eventClick, argClick)).setHoverEvent(new HoverEvent(eventHover, new TextComponentString(argHover))));
        return text;
    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, String argHover) {
        return this.getClicableMsg(StringUtils.formatMinecraftColors(textMsg), eventClick, argClick, HoverEvent.Action.SHOW_TEXT, StringUtils.formatMinecraftColors(argHover));
    }


}