package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.core.map.CustomToastGui;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.element.ElementSubMenu;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

@RegisterDisplayAction(templateId = "map_teleport")

public class ActionAdminTeleport implements DisplayAction {

    @Override
    public void perform(ActionData actionData) {

        Minecraft.getMinecraft().displayGuiScreen(null);

        String title = "GTWMap Manager";
        String description = "Teleportation...";
        ResourceLocation icon = ((ElementSubMenu)actionData.getAttachedElement()).getData().getIcon();
        CustomToastGui.showToast(title, description, icon, 2);

        String[] args = actionData.getActionArgs().getArgs();
        String x = args[0];
        String z = args[1];
        String y = args[2];

        if (args.length == 6) {
            String yaw = args[3];
            String pitch = args[4];
            String markerId = args[5];

            Minecraft.getMinecraft().player.sendChatMessage("/tppos " + x + " " + y + " " + z + " " + yaw + " " + pitch);
        } else {
            Minecraft.getMinecraft().player.sendChatMessage("/tppos " + x + " " + y + " " + z);
        }

        Minecraft.getMinecraft().player.sendChatMessage("/top");

        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§7============================================\n\n\n§8[MAP] §aYou have been successfully teleported.\n\n\n§7============================================"));
    }
}
