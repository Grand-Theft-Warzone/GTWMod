package com.grandtheftwarzone.gtwmod.core.map.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.MinimapManager;
import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayManager;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

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

    @Getter
    private int minZoom = 100, maxZoom = 1000;

    private boolean active = false;

    @Getter @Setter
    private boolean canActivated = true;

    @Getter
    private boolean allowedToDisplay = false;

    private DisplayElement element;

    private List<AtumColor> colorsFrame = new ArrayList<>();

    private AtumColor defaultColorFrame = AtumColor.GRAY;

    @Getter
    private ColorFilter colorFilter = new ColorFilter(AtumColor.RED, 0);

    private MapImage minimapData;

    @Getter @Setter
    private boolean initElementDraw = false;
    private ResourceLocation minimapImage;
    private ResourceLocation radarImage;



    public GtwMinimapManager(){
        EVENT_BUS.register(this);
    }



    public GtwMinimapManager(MapImage minimapData, ResourceLocation radarImage){
        this.minimapData = minimapData;
        this.minimapImage = minimapData.getImage();
        this.radarImage = radarImage;


        EVENT_BUS.register(this);
    }

    public void updateData(MapImage minimapData, ResourceLocation radarImage, boolean draw) {
        this.minimapData = minimapData;
        this.minimapImage = minimapData.getImage();
        this.radarImage = radarImage;

        this.initElementDraw = false;
        setAllowedToDisplay(draw, true);
    }

    public void updateData(MapImage minimapData, ResourceLocation radarImage) {
        this.minimapData = minimapData;
        this.minimapImage = minimapData.getImage();
        this.radarImage = radarImage;

        this.initElementDraw = false;
        setAllowedToDisplay(this.allowedToDisplay, true);
    }


    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (!allowedToDisplay) return;
        if (showMinimaps.isPressed()) {

            setActive(!active);
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (!active || !allowedToDisplay) return;

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
    public void setColorFilter(ColorFilter filter) {
        this.colorFilter = filter;
    }

    @Override
    public MapImage getMinimapImage() {
        return this.minimapData;
    }


    public void onPreInit(FMLPreInitializationEvent event) {
        showMinimaps = new KeyBinding("key.minimap.show.desc", Keyboard.KEY_M, "key.categories.mod");
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


    public void updateZoomLimits(int min, int max) {

        DisplayRenderer renderer = GtwAPI.getInstance().getGtwMod().getDisplayManager().getHUDCanvas().getDisplayRenderer();
        if (renderer == null) {
            System.out.println("НЕ ДАЛА(((");
        }
        DisplayElement element = renderer != null ? renderer.getBaseCanvas().getElement("minimap") : null;
        if (element == null) {
            System.out.println("NULLLLLLLLLLLL");
        }

        System.out.println(min + " " + max);
//        DisplayManager
        int zoom = Integer.parseInt(renderer.getDisplayData().getDataOrDefault("zoom_minimap", "250"));
        if (zoom < min) {
            element.performAction("zoom_minimap", "update_zoom;" + min);
        }
        if (zoom > max) {
            element.performAction("zoom_minimap", "update_zoom;" + max);
        }
        minZoom = min;
        maxZoom = max;
    }


    /**
     * Изменяет переменную allowedToDisplay и статус отображения.
     * @param draw значение переменной.
     * @param quietChange изменять ли значение переменной после изменения значения?
     */
    public void setAllowedToDisplay(Boolean draw, Boolean quietChange) {
        System.out.println(draw);
        if (draw == null) {System.out.println("Draw is null");}
        DisplayRenderer render = GtwAPI.getInstance().getGtwMod().getDisplayManager().getHUDCanvas()
                .getDisplayRenderer();
        if(render==null) return;
        if (quietChange) {
            render.getDisplayData().setElementEnabled("minimap", draw);
        }
        this.allowedToDisplay = draw;
    }

    @Override
    public void setColorFrame(AtumColor color, int time) {
        List<AtumColor> addColor = new ArrayList<>();
        for (int i = 0; i < time; i++) {
            addColor.add(color);
        }
        this.colorsFrame.addAll(addColor);
    }

}



