package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.grandtheftwarzone.gtwmod.api.misc.GLUtils.drawPartialImage;

@RegisterDisplayElement(templateId = "canvas_globalmap")
public class CanvasGlobalmap extends BaseCanvas {


    ResourceLocation minimapImage = new ResourceLocation("gtwmod:textures/gui/minimap/maps/general_map.png");

    public CanvasGlobalmap(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

//        MapImage mapImage = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage();
        RenderUtils.bindTexture(minimapImage);


        int zoom = 15000;
        System.out.println(zoom);
//        RenderUtils.drawCompleteImage(0, 0, getWidth(), getHeight());
        drawPartialImage(0, 0, getWidth(), getHeight(), 1000, 200, zoom, zoom);

    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

}
