package com.grandtheftwarzone.gtwmod.api.gui.phone.canvas;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;

public class CanvasPhoneApp extends BaseCanvas {
    private CanvasPhone canvasPhone;

    public CanvasPhoneApp(@NotNull AtumMod atumMod,
                          @NotNull CanvasPhone canvasPhone) {
        super(atumMod, null);
        this.canvasPhone = canvasPhone;
        setDisplayRenderer(canvasPhone.getDisplayRenderer());
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution,
                          float v, int i, int i1) {

    }

    @Override
    public int getGlobalX() {
        return super.getX() + canvasPhone.getGlobalX();
    }

    @Override
    public int getGlobalY() {
        return super.getY() + canvasPhone.getGlobalY();
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }
    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

}
