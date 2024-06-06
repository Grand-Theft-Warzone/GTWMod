package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.core.map.gui.GuiChatWithCommand;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

@RegisterDisplayAction(templateId = "map_delete_marker_server")
public class ActionAdminDeleteMarker implements DisplayAction {
    @Override
    public void perform(ActionData actionData) {

        String id = actionData.getActionArgs().getArgs()[0];

        Minecraft.getMinecraft().displayGuiScreen(new GuiChatWithCommand("/mapmanager remove " + id));

        MapMarker marker = GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getServerMarker(id);
        TextComponentString msgDelete = getClicableMsg("§7============================================\n\n\n§8[MAP] §ePress Enter to delete the marker.\n\n\n§7============================================", ClickEvent.Action.SUGGEST_COMMAND,  "/mapmanager remove " + marker.getIdentificator(), "§eClick to enter the delete command.");
        Minecraft.getMinecraft().player.sendMessage(msgDelete);
    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, HoverEvent.Action eventHover, String argHover) {
        TextComponentString text = new TextComponentString(textMsg);
        text.setStyle(new Style().setClickEvent(new ClickEvent(eventClick, argClick)).setHoverEvent(new HoverEvent(eventHover, new TextComponentString(argHover))));
        return text;
    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, String argHover) {
        return this.getClicableMsg(StringUtils.formatMinecraftColors(textMsg), eventClick, argClick, HoverEvent.Action.SHOW_TEXT, StringUtils.formatMinecraftColors(argHover));
    }
}
