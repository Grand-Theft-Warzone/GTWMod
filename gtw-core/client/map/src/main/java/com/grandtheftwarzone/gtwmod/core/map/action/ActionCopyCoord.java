package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.core.map.CustomToastGui;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.util.ResourceLocation;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

import java.util.Objects;

@RegisterDisplayAction(templateId = "map_copy_coord")
public class ActionCopyCoord implements DisplayAction {
    @Override
    public void perform(ActionData actionData) {

        Objects.requireNonNull(actionData.getAttachedElement()).getElementOwner().onRemove();

        String[] args = actionData.getActionArgs().getArgs();

        String text = args[0] + " " + args[2] + " " + args[1];
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

        String title = "GTWMap Manager";
        String description = "Coordinates copied.";
        ResourceLocation icon = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage("system/coord");
        CustomToastGui.showToast(title, description, icon);
    }
}
