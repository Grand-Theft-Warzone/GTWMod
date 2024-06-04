package com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.renderer.entity.Render;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CanvasMapSubmenu extends BaseCanvas {
    public CanvasMapSubmenu(@NotNull AtumMod atumMod, @NotNull DisplayLayer layer, int x, int y, int width, int height, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, layer, x, y, width, height, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {
        RenderUtils.drawRect(50, 50, 1000, 1000, AtumColor.YELLOW);
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }
}
