package com.grandtheftwarzone.gtwmod.core.map.gui;

import net.minecraft.client.gui.GuiChat;

public class GuiChatWithCommand extends GuiChat {
    private final String command;

    public GuiChatWithCommand(String command) {
        this.command = command;
    }

    @Override
    public void initGui() {
        super.initGui();
        inputField.setText(command);
    }
}
