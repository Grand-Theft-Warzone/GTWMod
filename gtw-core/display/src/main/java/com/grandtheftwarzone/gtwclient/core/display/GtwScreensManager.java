package com.grandtheftwarzone.gtwclient.core.display;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import com.grandtheftwarzone.gtwclient.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwclient.core.display.loadingscreen.transform.GtwLoadingScreen;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class GtwScreensManager implements ScreensManager {
    private CustomMainMenu customMainMenu;

    @Override
    public @NotNull GuiScreen getMainMenuGuiScreen() {
        if(customMainMenu != null){
            customMainMenu.getCanvas().onRemove();
        }

        customMainMenu = new CustomMainMenu();
        return customMainMenu;
    }

    private static class CustomMainMenu extends BaseScreen {

        public CustomMainMenu() {
            super(GtwAPI.getInstance().getGtwMod(),
                    (DisplayCanvas)( GtwAPI.getInstance().getGtwMod().getDisplayElementRegistry().getCanvasById(
                            GtwAPI.getInstance().getGtwMod().
                                    getConfigManager().getConfig("settings")
                                    .getString("main_menu")
                    ).clone()));
        }
    }
}
