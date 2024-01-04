package com.grandtheftwarzone.gtwmod.core.display.gui;

import com.google.common.collect.Lists;
import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseRenderer;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.api.placeholders.types.injectable.StaticPlaceholder;

public class FactoryGuiRenderer extends BaseRenderer {
    public FactoryGuiRenderer(DisplayCanvas baseCanvas,
                              BaseScreen attachedGuiScreen) {
        super(GtwAPI.getInstance().getGtwMod(),
                baseCanvas,
                attachedGuiScreen
        );

    }

    @Override
    public void onReload() {
        super.onReload();
    }
}
