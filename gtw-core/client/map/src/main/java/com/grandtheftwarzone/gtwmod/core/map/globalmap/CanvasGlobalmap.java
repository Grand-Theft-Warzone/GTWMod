package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.misc.GlobalZoom;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.input.InputPressEvent;
import me.phoenixra.atumodcore.api.events.input.InputReleaseEvent;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.grandtheftwarzone.gtwmod.api.misc.GLUtils.drawPartialImage;

@RegisterDisplayElement(templateId = "canvas_globalmap")
public class CanvasGlobalmap extends BaseCanvas {

    boolean isActive = false;
    private MapImage globalmap;
    private ResourceLocation globalmapTexture;

    private MapLocation imageLocation;

    private GlobalZoom zoom;

    private boolean pressIncreaseZoom;
    private boolean pressDecreaseZoom;

    public CanvasGlobalmap(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

        // Проверка, нужна ли инициализация
        boolean init = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().isInitCanvasDraw();
        isActive = init;
        if (!init) {
            init();
            return;
        }

        // Блок отслеживания нажатия на клавиши зума.

        // Приближение
        if (pressIncreaseZoom) {
            System.out.println("Приближение...");
            zoom.addZoom(getSettingsConfig().getSubsection("settings").getInt("step_zoom"));
        } else if (pressDecreaseZoom) {
            System.out.println("Отдаление...");
            zoom.removeZoom(getSettingsConfig().getSubsection("settings").getInt("step_zoom"));
        }

        RenderUtils.bindTexture(globalmapTexture);

        int lastZoom = zoom.getZoom();
        drawPartialImage(0, 0, getWidth(), getHeight(), (int)imageLocation.getX()  - (lastZoom / 2), (int)imageLocation.getY()  - (lastZoom / 2), lastZoom, lastZoom);

    }



    private void init() {
        // БлаБлаБла
        // @TODO remove
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) {
            return;
        }

        zoom = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom();
        globalmap = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage();
        globalmapTexture = globalmap.getImage();

        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(true);

    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
        System.out.println("Вызываю updateElementVariables в CanvasGlobalmap");
        String debugCordStr = config.getString("debug_cord");
        imageLocation = new MapLocation(debugCordStr);

        // zoom
        int configZoom = config.getInt("zoom");
//        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().setStraightZoom(configZoom);
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().setStraightZoom(configZoom);

        isActive = false;
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(false);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        System.out.println("Вызываю onRemove в CanvasGlobalmap");
        isActive = false;
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(false);

        LoadableConfig config = (LoadableConfig) getSettingsConfig();
        config.getSubsection("settings").set("zoom", GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalZoom().getLastZoomAndClearList());
        try {
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }


    @SubscribeEvent
    protected void onPress(InputPressEvent event) {

        if (!isActive()) {
            return;
        }

        // Увеличиваю зум.
        if (event.getKeyboardKey() == GtwAPI.getInstance().getMapManagerClient().getKeyIncreaseZoom().getKeyCode() && !pressDecreaseZoom) {
            System.out.println("Включаю pressIncreaseZoom");
            pressIncreaseZoom = true;
        } else if (event.getKeyboardKey() == GtwAPI.getInstance().getMapManagerClient().getKeyDecreaseZoom().getKeyCode() && !pressIncreaseZoom) {
            System.out.println("Включаю pressDecreaseZoom");
            pressDecreaseZoom = true;
        }

    }

    @SubscribeEvent
    protected void onRelease(InputReleaseEvent event) {
        if(!isActive()){
            return;
        }

        // Увеличиваю зум стоп.
        if (event.getKeyboardKey() == GtwAPI.getInstance().getMapManagerClient().getKeyIncreaseZoom().getKeyCode()) {
            System.out.println("Выключаю pressIncreaseZoom");
            pressIncreaseZoom = false;
        } else if (event.getKeyboardKey() == GtwAPI.getInstance().getMapManagerClient().getKeyDecreaseZoom().getKeyCode()) {
            System.out.println("Выключаю pressDecreaseZoom");
            pressDecreaseZoom = false;
        }

    }
}
