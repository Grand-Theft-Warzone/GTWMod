package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GtwGlobalmapScreen extends BaseScreen {

    public GtwGlobalmapScreen(@NotNull AtumMod atumMod, @NotNull String id) {
        super(atumMod, Objects.requireNonNull(atumMod.getDisplayManager().getElementRegistry().getDrawableCanvas(id)));
    }

}