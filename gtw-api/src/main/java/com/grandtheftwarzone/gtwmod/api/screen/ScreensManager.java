package com.grandtheftwarzone.gtwmod.api.screen;

import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public interface ScreensManager {


    /**
     * Get the Gui screen instance for main menu
     *
     * @return The Gui Screen
     */
    @NotNull GuiScreen getMainMenuGuiScreen();

    /**
     * Get the loading screen class name.
     * It has to be a class transformer that will be used
     * by FML to transform the vanilla loading screen.
     *
     * @return The loading screen class name
     */
    static @NotNull String getLoadingScreenClassName() {
        return "com.grandtheftwarzone.gtwmod.core.display.loadingscreen.transform.GtwLoadingScreen";
    }
}
