package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;


/**
 * Idea: have a canvas that might be used as an app drawer, that will be
 * linked with phone display
 *
 *
 */
public class CanvasApp extends BaseCanvas {
    private CanvasPhone phone;


    public CanvasApp(@NotNull AtumMod atumMod,
                     @NotNull CanvasPhone phone) {
        super(atumMod);
        this.phone = phone;
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution,
                          float v, int i, int i1) {

    }

    @Override
    public void addElement(@NotNull DisplayElement element) {
        //element.setAdditionX();
        super.addElement(element);
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
