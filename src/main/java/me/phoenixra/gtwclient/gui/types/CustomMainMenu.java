package me.phoenixra.gtwclient.gui.types;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.mod.test.TestMenu;
import me.phoenixra.gtwclient.GTWClient;

public class CustomMainMenu extends BaseScreen {
    private static CustomMainMenu instance;
    public CustomMainMenu() {
        super(
                GTWClient.instance,
                (DisplayCanvas)( GTWClient.instance.getDisplayElementRegistry().getCanvasById(
                        GTWClient.instance.getSettings().getString("main_menu")
                ).clone())
        );
        if(instance != null) {
            instance.getCanvas().onRemove();
        }
        instance = this;
    }
}
