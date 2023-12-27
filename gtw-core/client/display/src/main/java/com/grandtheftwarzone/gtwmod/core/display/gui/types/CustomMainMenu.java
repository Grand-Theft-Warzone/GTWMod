package com.grandtheftwarzone.gtwmod.core.display.gui.types;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;

import java.util.Objects;

public class CustomMainMenu extends BaseScreen {
    private static CustomMainMenu instance;
    public CustomMainMenu() {
        super(
                GtwAPI.getInstance().getGtwMod(),
                (DisplayCanvas)(Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().getDisplayManager().getElementRegistry().getDrawableCanvas(
                        Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().getConfigManager().getConfig("settings"))
                                .getString("main_menu")
                )))
        );
        if(instance != null) {
            instance.getCanvas().onRemove();
        }
        instance = this;
    }
}
