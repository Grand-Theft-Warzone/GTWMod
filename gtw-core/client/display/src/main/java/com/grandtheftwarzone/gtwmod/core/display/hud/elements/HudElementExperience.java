package com.grandtheftwarzone.gtwmod.core.display.hud.elements;

import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementExperience extends HudElement {
    public HudElementExperience() {
        super(HudElementType.EXPERIENCE, 0, 0, 0, 0);
    }

    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        int posXLocal = scaledWidth - 80 - 15;
        int posYLocal = 65;
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        double exp = pd.getExperience();
        double expCap = pd.getExperienceCap();
        double full = 100D / expCap;
        GlStateManager.disableLighting();
        drawCustomBar(posXLocal,
                posYLocal,
                95,
                8,
                exp * full,
                0xAEED7A,
                0xAEED7A,
                true
        );
    }
}
