package com.grandtheftwarzone.gtwclient.core.display.hud.elements;

import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementLvl extends HudElement {
    public HudElementLvl() {
        super(HudElementType.LEVEL, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        float posXLocal = scaledWidth-80 - 15;
        float posYLocal = 56;
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        String level = "\u272a "+pd.getLevel();
        mc.fontRenderer.drawStringWithShadow(level,
                posXLocal,
                posYLocal,
                COLOR_YELLOW);

    }
}
