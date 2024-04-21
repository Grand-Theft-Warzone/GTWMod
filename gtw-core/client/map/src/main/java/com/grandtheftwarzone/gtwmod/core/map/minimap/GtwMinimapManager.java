package com.grandtheftwarzone.gtwmod.core.map.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.MinimapManager;
import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import com.grandtheftwarzone.gtwmod.api.map.data.client.UpdateMinimapData;
import com.grandtheftwarzone.gtwmod.api.map.MapImageUtils;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.GtwGlobalmapScreen;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;


public class GtwMinimapManager implements MinimapManager {


    private static KeyBinding increaseZoom;


    private static KeyBinding decreaseZoom;


    private static KeyBinding showMinimaps;

    private DisplayRenderer renderer;

    private boolean saveZoom = true;

    @Getter @Setter
    private Integer minZoom, maxZoom;

    private boolean active = false;

    @Getter @Setter
    private boolean canActivated = true;

    @Getter @Setter
    private UpdateMinimapData updatingData = null;

    private DisplayElement element;

    private List<AtumColor> colorsFrame = new ArrayList<>();

    @Getter
    private AtumColor defaultColorFrame = AtumColor.GRAY;

    @Getter @Setter
    private float opacityFilter = 0;

    private MapImage minimapData;

    @Getter @Setter
    private boolean initElementDraw = false;
    @Getter @Setter
    private AtumColor colorBorderReach = null;
    private ResourceLocation minimapImage;
    private ResourceLocation radarImage;



    public GtwMinimapManager(){
        EVENT_BUS.register(this);
    }



    public void updateData(MapImageData minimapData, String radarImageId, Boolean draw) {

        ResourceLocation mapTexture = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getMapImage(minimapData.getImageId(), minimapData.getColorBackground());
        this.minimapData = new MapImage(mapTexture, minimapData.getImageId(), minimapData.getTopRight(), minimapData.getDownRight(), minimapData.getDownLeft(), minimapData.getTopLeft(), minimapData.getOffsetX(), minimapData.getOffsetY());
        this.minimapImage = mapTexture;
        this.radarImage = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage(radarImageId);

        this.colorBorderReach = minimapData.getColorBorderReach();
        this.initElementDraw = false;
        if (draw == null) {
            draw = GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay();
        }
        GtwAPI.getInstance().getMapManagerClient().setAllowedToDisplay(draw, true);
    }



    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) return;
        if (showMinimaps.isPressed()) {

            setActive(!active);
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (!active || !GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) return;

        if (increaseZoom.isKeyDown()) {
            element.performAction("zoom_minimap", "add");
            saveZoom = false;
            return;
        } else if (decreaseZoom.isKeyDown()) {

            element.performAction("zoom_minimap", "remove");
            saveZoom = false;
            return;
        }

        if (!saveZoom) {
            element.performAction("zoom_minimap", "update_default");
            saveZoom = true;
            colorsFrame.clear();
        }
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event) {
        if (updatingData != null) {
            if (updatingData.getAllowDisplay() != null) {
                updateData(updatingData.getMapImageData(), updatingData.getRadarImageId(), updatingData.getAllowDisplay());
            } else {
                updateData(updatingData.getMapImageData(), updatingData.getRadarImageId(), null);
            }

            this.updatingData = null;
        }
    }

    public void setActive(boolean active) {

        if (canActivated) {
            this.active = active;
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
            GtwLog.getLogger().debug("Minimap visibility changed: " + active);

//            // @TODO Переделать!!!
//            if (active) {
//                Minecraft.getMinecraft().displayGuiScreen(
//                        new GtwGlobalmapScreen(GtwAPI.getInstance().getGtwMod(), "globalmap")
//                );
//            } else {
//                Minecraft.getMinecraft().displayGuiScreen(
//                        null
//                );
//            }

        } else {
            GtwLog.getLogger().debug("Changing minimap activity is prohibited.");
            renderer.getDisplayData().setTemporaryData("notification", "&eChanging minimap activity is prohibited.", 40, false);
        }

    }

    public void updateMinimapManager(DisplayRenderer renderer) {
        this.renderer = renderer;
        this.element = renderer.getBaseCanvas().getElement("minimap");
        this.active = renderer.getDisplayData().getDataOrDefault("active_minimap", "1").equals("1");
        setActive(this.active);

    }


    public ResourceLocation getResourceLocation(String imageName) {
        if (imageName.equals("minimapImage")) {
            return minimapImage;
        } else if (imageName.equals("radarImage")) {
            return radarImage;
        }

        return null;
    }


    @Override
    public MapImage getMinimapImage() {
        return this.minimapData;
    }


    public void onPreInit(FMLPreInitializationEvent event) {
        showMinimaps = new KeyBinding("key.minimap.show.desc", Keyboard.KEY_U, "key.categories.mod");
        increaseZoom = new KeyBinding("key.minimap.increase.desc", Keyboard.KEY_PRIOR, "key.categories.mod");
        decreaseZoom = new KeyBinding("key.minimap.decrease.desc", Keyboard.KEY_NEXT, "key.categories.mod");

        ClientRegistry.registerKeyBinding(increaseZoom);
        ClientRegistry.registerKeyBinding(decreaseZoom);
        ClientRegistry.registerKeyBinding(showMinimaps);

    }

    @Override
    public boolean isActive() {
        return active;
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


    public void updateZoomLimits(Integer min, Integer max) {

        DisplayRenderer renderer = GtwAPI.getInstance().getGtwMod().getDisplayManager().getHUDCanvas().getDisplayRenderer();
        if (renderer == null) {
            System.out.println("НЕ ДАЛА(((");
        }
        DisplayElement element = renderer != null ? renderer.getBaseCanvas().getElement("minimap") : null;
        if (element == null) {
            System.out.println("NULLLLLLLLLLLL");
        }

        System.out.println(min + " : " + max);
//        DisplayManager
        int zoom = Integer.parseInt(renderer.getDisplayData().getDataOrDefault("zoom_minimap", "250"));
        System.out.println("Сейчас zoom: " + zoom);
        if (min != null) {
            this.setMinZoom(min);
            if (zoom < min) {
                element.performAction("zoom_minimap", "update_zoom;" + min);
                System.out.println("Уменьшаю зум до " + min);
            }
        }
        if (max != null) {
            this.setMaxZoom(max);
            if (zoom > max) {
                element.performAction("zoom_minimap", "update_zoom;" + max);
                System.out.println("Уменьшаю зум до " + max);
            }
        }

    }


//    /**
//     * Изменяет переменную allowedToDisplay и статус отображения.
//     * @param draw значение переменной.
//     * @param quietChange изменять ли значение переменной после изменения значения?
//     */
//    public void setAllowedToDisplay(Boolean draw, Boolean quietChange) {
//        System.out.println(draw);
//        if (draw == null) {System.out.println("Draw is null");}
//        DisplayRenderer render = GtwAPI.getInstance().getGtwMod().getDisplayManager().getHUDCanvas()
//                .getDisplayRenderer();
//        if(render==null) return;
//        if (quietChange) {
//            render.getDisplayData().setElementEnabled("minimap", draw);
//        }
//        this.allowedToDisplay = draw;
//    }

    @Override
    public void setColorFrame(AtumColor color, int time) {
        List<AtumColor> addColor = new ArrayList<>();
        for (int i = 0; i < time; i++) {
            addColor.add(color);
        }
        this.colorsFrame.addAll(addColor);
    }

}



