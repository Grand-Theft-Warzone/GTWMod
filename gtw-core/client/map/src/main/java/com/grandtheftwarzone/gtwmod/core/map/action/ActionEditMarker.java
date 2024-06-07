package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

@RegisterDisplayAction(templateId = "map_edit_marker")
public class ActionEditMarker implements DisplayAction {
    @Override
    public void perform(ActionData actionData) {

        Minecraft.getMinecraft().displayGuiScreen(new GuiChat());

        String markerId = actionData.getActionArgs().getArgs()[0];

        MapMarker marker = GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getLocalMarker(markerId);

        if (marker == null) {
            String msgStr = "§e[GTWMap] §cToken with id §f" + markerId + "§c was not found! §7[Show markers]";
            TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/lmm list", "§8Show markers");
            Minecraft.getMinecraft().player.sendMessage(msg);
        } else {

            TextComponentString msg = new TextComponentString(StringUtils.formatMinecraftColors("§b---========§a<< " + marker.getName() + " §a>>§b========---\n"));

            TextComponentString msgId = getClicableMsg("\n§7• §fID: §8" + marker.getIdentificator(), ClickEvent.Action.SUGGEST_COMMAND, marker.getIdentificator(), "§8Copy ID");
            msg.appendSibling(msgId);

            String cmdPrefix = "/lmm edit " + marker.getIdentificator() + " ";

            TextComponentString msgName = getClicableMsg("\n\n§b• §aName: §f" + marker.getName(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "name " + marker.getName(), "§8Change name");
            msg.appendSibling(msgName);

            TextComponentString msgLore = getClicableMsg("\n§b• §aLore: §f" + marker.getLore(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "lore " + marker.getLore(), "§8Change description");
            msg.appendSibling(msgLore);

            TextComponentString msgIconId = getClicableMsg("\n§b• §aIcon Id: §f" + marker.getIconId(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "iconid " + marker.getIconId(), "§8Change icon id");
            msg.appendSibling(msgIconId);

            EntityLocation worldLocation = marker.getWorldLocation();
            TextComponentString msgWorldLocation = getClicableMsg("\n§e• §aWorldLocation: §f" + (int)worldLocation.getX() + " " + (int)worldLocation.getZ() + " " + (int)worldLocation.getY(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "worldlocation " + marker.getWorldLocation(), "§8Change worldLocation");
            msg.appendSibling(msgWorldLocation);

            TextComponentString msgDraw = getClicableMsg("\n\n§f• §aDraw: §f" + marker.isDraw(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "draw " + marker.isDraw(), "§8Change draw");
            msg.appendSibling(msgDraw);

            TextComponentString msgDelete = getClicableMsg("\n\n§c[DELITE]", ClickEvent.Action.SUGGEST_COMMAND,  "/lmm remove " + marker.getIdentificator(), "§eClick here to remove marker");
            msg.appendSibling(msgDelete);

            TextComponentString msgEnd = new TextComponentString("\n\n     §7(Click on parametr to edit) \n§b---==========================---");
            msg.appendSibling(msgEnd);

            Minecraft.getMinecraft().player.sendMessage(msg);
            return;
        }

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
