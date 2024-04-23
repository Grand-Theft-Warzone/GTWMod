package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.grandtheftwarzone.gtwmod.api.misc.GLUtils.drawPartialImage;

@RegisterDisplayElement(templateId = "canvas_globalmap")
public class CanvasGlobalmap extends BaseCanvas {

    private MapImage globalmap;
    private ResourceLocation globalmapTexture;

    private int zoom = 100;
    private MapLocation imageLocation;

    public CanvasGlobalmap(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

        boolean init = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().isInitCanvasDraw();
        if (!init) {
            init();
            return;
        }

        RenderUtils.bindTexture(globalmapTexture);



        drawPartialImage(0, 0, getWidth(), getHeight(), (int)imageLocation.getX()  - (zoom / 2), (int)imageLocation.getY()  - (zoom / 2), zoom, zoom);

    }



    private void init() {
        // БлаБлаБла

        // @TODO remove
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) {
            return;
        }

        globalmap = GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage();
        globalmapTexture = globalmap.getImage();

        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(true);

    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
        System.out.println("Вызываю updateElementVariables в CanvasGlobalmap");
        zoom = config.getInt("zoom");
        String debugCordStr = config.getString("debug_cord");
        imageLocation = new MapLocation(debugCordStr);

        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(false);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        System.out.println("Вызываю onRemove в CanvasGlobalmap");
        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setInitCanvasDraw(false);
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

}
