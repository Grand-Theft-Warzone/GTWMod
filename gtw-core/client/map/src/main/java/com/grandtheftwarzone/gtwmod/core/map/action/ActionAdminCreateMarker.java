package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MarkerCreationStateMachine;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

@RegisterDisplayAction(templateId = "map_create_marker_server")
public class ActionAdminCreateMarker implements DisplayAction {

    @Override
    public void perform(ActionData actionData) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        GtwAPI.getInstance().getMapManagerClient().setMarkerCreator(new MarkerCreationStateMachine(String.join(";", actionData.getActionArgs().getArgs()), true));
    }
}
