package com.grandtheftwarzone.gtwmod.core.phone.core;

import com.grandtheftwarzone.gtwmod.core.phone.core.canvas.CanvasPhone;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;

public class GtwPhoneScreen extends BaseScreen {
    public GtwPhoneScreen(@NotNull AtumMod atumMod) {
        super(atumMod, new CanvasPhone(atumMod));
    }



    public void closeAnimated(){

    }
}
