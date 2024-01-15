package com.grandtheftwarzone.gtwmod.core.display.gui;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CanvasFactoriesGui extends BaseCanvas {
    public CanvasFactoriesGui(@NotNull DisplayLayer layer, int x, int y, int width, int height, @Nullable DisplayCanvas elementOwner) {
        super(GtwAPI.getInstance().getGtwMod(), layer, x, y, width, height, elementOwner);
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas baseCanvas) {
        return null;
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }

    @Override
    protected void onDraw(float v, float v1, float v2, int i, int i1) {

    }
}