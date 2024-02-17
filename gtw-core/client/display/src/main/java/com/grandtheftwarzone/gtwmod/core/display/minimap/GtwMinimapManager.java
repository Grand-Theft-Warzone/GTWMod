package com.grandtheftwarzone.gtwmod.core.display.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.gui.minimap.MinimapManager;
import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class GtwMinimapManager implements AtumModService, MinimapManager {


    private static KeyBinding increaseZoom;


    private static KeyBinding decreaseZoom;


    private static KeyBinding showMinimaps;

    private DisplayRenderer renderer;

    private boolean isSaveZoom = true;

    private boolean isActive;

    @Getter @Setter
    private boolean canActivated = true;

    private DisplayElement element;

    private List<AtumColor> colorsFrame = new ArrayList<>();

    private AtumColor defaultColorFrame = AtumColor.GRAY;

    private ColorFilter colorFilter = new ColorFilter(AtumColor.RED, 0);


    private ResourceLocation minimapImage = new ResourceLocation("gtwmod", "textures/gui/minimap/test_map.png");
    private ResourceLocation radarImage = new ResourceLocation("gtwmod", "textures/gui/minimap/radar.png");



    public GtwMinimapManager(AtumMod atumMod){
        atumMod.provideModService(this);
        this.minimapImage = new ResourceLocation("gtwmod", "textures/gui/minimap/test_map.png");
        this.radarImage = new ResourceLocation("gtwmod", "textures/gui/minimap/radar.png");
    }


    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (showMinimaps.isPressed()) {

            setActive(!isActive);
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (!isActive) return;

        if (increaseZoom.isKeyDown()) {
            element.performAction("zoom_minimap", "add");
            isSaveZoom = false;
            return;
        } else if (decreaseZoom.isKeyDown()) {

            element.performAction("zoom_minimap", "remove");
            isSaveZoom = false;
            return;
        }

        if (!isSaveZoom) {
            element.performAction("zoom_minimap", "update_default");
            isSaveZoom = true;
            colorsFrame.clear();
        }
    }

    public void setActive(boolean active) {

        if (canActivated) {
            isActive = active;
            Config config = (Config) renderer.getBaseCanvas().getSettingsConfig();
            String strActive;
            if (active) {strActive = "1";} else {strActive = "0";}
            renderer.getBaseCanvas().getSettingsConfig().getSubsection("default_data").set("active_minimap", strActive);
            try {
                assert config != null;
                ((LoadableConfig)config).save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            GtwLog.debug("Minimap visibility changed: " + active);
        } else {
            GtwLog.debug("Changing minimap activity is prohibited.");
            renderer.getDisplayData().setTemporaryData("notification", "&eChanging minimap activity is prohibited.", 40, false);
        }

    }

    public void updateMinimapManager(DisplayRenderer renderer) {
        this.renderer = renderer;
        this.element = renderer.getBaseCanvas().getElement("minimap");
        this.isActive = renderer.getDisplayData().getDataOrDefault("active_minimap", "1").equals("1");
        setActive(this.isActive);

    }


    public ResourceLocation getResourceLocation(String imageName) {
        if (imageName.equals("minimapImage")) {
            return minimapImage;
        } else if (imageName.equals("radarImage")) {
            return radarImage;
        }

        return null;
    }

    public ColorFilter getColorFilter() {
        return colorFilter;
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        this.colorFilter = filter;
    }


    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLPreInitializationEvent){
            onPreInit((FMLPreInitializationEvent) fmlEvent);
        }
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        showMinimaps = new KeyBinding("key.minimap.show.desc", Keyboard.KEY_M, "key.categories.mod");
        increaseZoom = new KeyBinding("key.minimap.increase.desc", Keyboard.KEY_PRIOR, "key.categories.mod");
        decreaseZoom = new KeyBinding("key.minimap.decrease.desc", Keyboard.KEY_NEXT, "key.categories.mod");

        ClientRegistry.registerKeyBinding(increaseZoom);
        ClientRegistry.registerKeyBinding(decreaseZoom);
        ClientRegistry.registerKeyBinding(showMinimaps);
    }

    @Override
    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public AtumColor getColorFrame() {
        if (colorsFrame.isEmpty()) {
            return defaultColorFrame;
        } else {
            return colorsFrame.remove(0);
        }
    }

    @Override
    public void setDefaultColorFrame(AtumColor color) {
        this.defaultColorFrame = color;
    }
    @Override
    public void setColorFrame(AtumColor color, int time) {
        List<AtumColor> addColor = new ArrayList<>();
        for (int i = 0; i < time; i++) {
            addColor.add(color);
        }
        this.colorsFrame.addAll(addColor);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "minimap";
    }

}



