package com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas;

import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.renderer.entity.Render;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CanvasMapSubmenu extends BaseCanvas {

    @Getter @Setter
    private DisplayElement elementClick;

    @Getter @Setter
    private boolean admin;

    public CanvasMapSubmenu(@NotNull AtumMod atumMod, @NotNull DisplayLayer layer, int x, int y, @Nullable DisplayCanvas elementOwner, @Nullable DisplayElement elementClick) {
        super(atumMod, layer, x, y, 0, 0, elementOwner);
        this.elementClick = elementClick;
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {
        System.out.println("Пытаюсь отобразить");
        RenderUtils.drawRect(getX(), getY(), 300, 500, AtumColor.YELLOW);
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        System.out.println("Вызываю onRemove в CanvasMapSubmenu");
        ((CanvasGlobalmap) getElementOwner()).setSubCanvas(null);
    }

}
