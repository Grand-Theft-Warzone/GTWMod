package com.grandtheftwarzone.gtwmod.core.map.action;


import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

@RegisterDisplayAction(templateId = "map_edit_marker_server")
public class ActionAdminEditMarker implements DisplayAction {

    @Override
    public void perform(ActionData actionData) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        Minecraft.getMinecraft().player.sendChatMessage("/mapmanager info " + actionData.getActionArgs().getArgs()[0]);
    }

}
