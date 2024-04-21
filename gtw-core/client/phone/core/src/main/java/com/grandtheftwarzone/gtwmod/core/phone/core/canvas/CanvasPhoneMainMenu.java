package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CanvasPhoneMainMenu extends BaseCanvas {
    protected CanvasPhone phone;
    public CanvasPhoneMainMenu(@NotNull AtumMod atumMod,
                               @NotNull CanvasPhone phone
    ) {
        super(atumMod, phone);
        this.phone = phone;
    }


    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    private boolean isAppHovered(int x, int y, int size,
                                 int mouseX, int mouseY) {
        return mouseX >= x &&
                mouseX <= x + size
                && mouseY >= y
                && mouseY <= y + size;
    }
}
