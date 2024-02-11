package com.grandtheftwarzone.gtwmod.api.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.IGuiHandler;

public interface FactoryGuiHandler extends IGuiHandler {


    /**
     * Open the factory gui
     *
     * @param factoryType the factory type
     * @param guiSession the gui session
     */
    void openFactoryGui(String factoryType, GuiSession guiSession);


    void closeGUI();
    void reloadGUI();

    //@TODO move to other place
    GuiScreen getCurrentGui();
    void setCurrentGui(GuiScreen guiScreen);

    boolean isFactoryGuiOpened();
    int getGuiID();
    GuiSession getGuiSession();


}
