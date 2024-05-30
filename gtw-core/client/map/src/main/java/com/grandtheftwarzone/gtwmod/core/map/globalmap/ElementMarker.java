package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseStaticMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.PlayerMarker;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVarInt;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementMarker extends BaseElement {

    MapMarker marker;

    public ElementMarker(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner, MapMarker marker, int size) {
        super(atumMod, DisplayLayer.MIDDLE, 0, 0, size, size, elementOwner);
        this.marker = marker;

    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {


//
//            RenderUtils.bindTexture(marker.getIcon());
//            RenderUtils.drawCompleteImage(getX(), getY(), getWidth(), getWidth());

        RenderUtils.drawRect(getX(), getY(), getWidth(), getHeight(), AtumColor.BLUE);
    }


    public void setXY(int x, int y) {
        getOriginX().setDefaultValue(x);
        getOriginY().setDefaultValue(y);
    }

    public void setSize(int size) {

        getOriginWidth().setDefaultValue(size);
        getOriginHeight().setDefaultValue(size);
    }

    public void setXY(MapLocation location) {
        this.setXY((int) location.getX(), (int) location.getY());
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
