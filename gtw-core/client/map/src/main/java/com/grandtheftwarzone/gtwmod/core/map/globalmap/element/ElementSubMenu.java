package com.grandtheftwarzone.gtwmod.core.map.globalmap.element;

import com.grandtheftwarzone.gtwmod.api.utils.GLUtils;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.data.DataMapSubMenu;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementSubMenu extends BaseElement {

    private DataMapSubMenu data;

    public ElementSubMenu(@NotNull AtumMod atumMod, int drawPriority, int x, int y, int width, int height, @Nullable DisplayCanvas elementOwner, DataMapSubMenu data) {
        super(atumMod, drawPriority, x, y, width, height, elementOwner);
        this.data = data;
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {
        GLUtils.drawText(getX(), getY(), data.getName(), data.getColorName(), data.getSizeText());
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
