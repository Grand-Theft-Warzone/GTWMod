package com.grandtheftwarzone.gtwmod.core.display.hud.elements;

import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElementType;
import net.minecraft.client.gui.Gui;

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
