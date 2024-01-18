package com.grandtheftwarzone.gtwmod.core.phone.core;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GtwPhoneScreen extends BaseScreen {
    public GtwPhoneScreen(@NotNull AtumMod atumMod, @NotNull String id) {
        super(atumMod, Objects.requireNonNull(atumMod.getDisplayManager()
                .getElementRegistry().getDrawableCanvas(id))
        );
    }



    public void closeAnimated(){
        getRenderer().getDisplayData().setData("close", "true");
    }
}
