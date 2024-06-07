package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.emoji.RLEmoji;
import com.grandtheftwarzone.gtwmod.api.map.marker.MarkerCreationStateMachine;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.element.ElementSubMenu;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.List;

@RegisterDisplayAction(templateId = "map_create_marker")
public class ActionCreateMarker implements DisplayAction {
    @Override
    public void perform(ActionData actionData) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
        GtwAPI.getInstance().getMapManagerClient().setMarkerCreator(new MarkerCreationStateMachine(String.join(";", actionData.getActionArgs().getArgs()), false));
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
