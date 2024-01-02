package com.grandtheftwarzone.gtwmod.core.display;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GtwScreensManager implements ScreensManager {
    private CustomMainMenu customMainMenu;

    @Override
    public @NotNull GuiScreen getMainMenuGuiScreen() {
        if(customMainMenu != null){
            customMainMenu.getRenderer().closeRenderer();
        }

        customMainMenu = new CustomMainMenu();
        return customMainMenu;
    }

    private static class CustomMainMenu extends BaseScreen {

        public CustomMainMenu() {
            super(GtwAPI.getInstance().getGtwMod(),
                    Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().getDisplayManager().getElementRegistry().getDrawableCanvas(
                            Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().
                                            getConfigManager().getConfig("settings"))
                                    .getString("main_menu")
                    )));
        }
    }
}
