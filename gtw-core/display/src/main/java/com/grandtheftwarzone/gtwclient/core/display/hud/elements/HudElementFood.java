package com.grandtheftwarzone.gtwclient.core.display.hud.elements;

import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementFood extends HudElement {
    public HudElementFood() {
        super(HudElementType.FOOD, 0, 0, 0, 0);
    }

    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        int posXLocal = scaledWidth - 80 - 15;
        int posYLocal = 23;
        EntityPlayerSP player = mc.player;
        int stamina = player.getFoodStats().getFoodLevel();
        int staminaMax = 20;

        GlStateManager.disableLighting();
        drawCustomBar(
                posXLocal,
                posYLocal,
                95,
                8,
                stamina / (double) staminaMax * 100.0D,
                COLOR_BROWN,
                COLOR_BROWN,
                true
        );
    }
}
