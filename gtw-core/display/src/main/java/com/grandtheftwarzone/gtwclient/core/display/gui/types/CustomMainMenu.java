package com.grandtheftwarzone.gtwclient.core.display.gui.types;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;

public class CustomMainMenu extends BaseScreen {
    private static CustomMainMenu instance;
    public CustomMainMenu() {
        super(
                GtwAPI.getInstance().getGtwMod(),
                (DisplayCanvas)( GtwAPI.getInstance().getGtwMod().getDisplayElementRegistry().getCanvasById(
                        GtwAPI.getInstance().getGtwMod().getConfigManager().getConfig("settings")
                                .getString("main_menu")
                ).clone())
        );
        if(instance != null) {
            instance.getCanvas().onRemove();
        }
        instance = this;
    }
}
