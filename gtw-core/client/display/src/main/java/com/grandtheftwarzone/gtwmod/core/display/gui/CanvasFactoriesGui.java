package com.grandtheftwarzone.gtwmod.core.display.gui;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CanvasFactoriesGui extends BaseCanvas {
    public CanvasFactoriesGui(@NotNull DisplayLayer layer, int x, int y, int width, int height, @Nullable DisplayCanvas elementOwner) {
        super(GtwAPI.getInstance().getGtwMod(), layer, x, y, width, height, elementOwner);
    }

    @Override
    protected BaseElement onClone(BaseElement baseCanvas) {
        return null;
    }



    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

    }
}
