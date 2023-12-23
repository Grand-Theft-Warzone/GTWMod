package com.grandtheftwarzone.gtwmod.api.hud;

import net.minecraft.client.gui.Gui;

//@TODO update it to use atummodcore display lib
public interface PlayerHUD {

    /**
     * Draw HUD
     *
     * @param gui the gui attached
     * @param screenWidth width of the screen
     * @param screenHeight height of the screen
     */
    void draw(Gui gui, int screenWidth, int screenHeight);
}
