package com.grandtheftwarzone.gtwclient.core.display.hud.elements;

import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementRank extends HudElement {
    public HudElementRank() {
        super(HudElementType.RANK, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }
    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        float posXLocal = scaledWidth-80 - 15;
        float posYLocal = 46;
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        String rank = pd.getRank();
        mc.fontRenderer.drawStringWithShadow(rank,
                posXLocal,
                posYLocal,
                0xAEED7A
        );

    }
}
