package com.grandtheftwarzone.gtwmod.core.map.globalmap.element;

import com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas.CanvasMapSubmenu;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementCastil extends BaseElement {
    public ElementCastil(@NotNull AtumMod atumMod, int drawPriority, int x, int y, int width, int height, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, drawPriority, x, y, width, height, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {
        if (getElementOwner() instanceof CanvasMapSubmenu) {
            ((CanvasMapSubmenu) getElementOwner()).setFixCoordsX(getX());
            ((CanvasMapSubmenu) getElementOwner()).setFixCoordsY(getY());
            ((CanvasMapSubmenu) getElementOwner()).setFixWidth(getWidth());
            ((CanvasMapSubmenu) getElementOwner()).setFixHeight(getHeight());

        }
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return null;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
