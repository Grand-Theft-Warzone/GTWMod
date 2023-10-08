package com.grandtheftwarzone.gtwclient.core.display.hud.elements;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.ForgeHooks;

public class HudElementArmor extends HudElement {
    public HudElementArmor() {
        super(HudElementType.ARMOR, 0, 0, 0, 0);
    }
    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        int posXLocal = scaledWidth - 80 - 15;
        int posYLocal = 36;
        int armor = ForgeHooks.getTotalArmorValue(mc.player);
        int armorMax = 20;

        GlStateManager.disableLighting();
        drawCustomBar(
                posXLocal,
                posYLocal,
                95,
                8,
                armor / (double) armorMax * 100.0D,
                AtumColor.LIGHT_GRAY.toInt(),
                AtumColor.LIGHT_GRAY.toInt(),
                true
        );
    }
}
