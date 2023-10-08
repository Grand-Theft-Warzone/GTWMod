package com.grandtheftwarzone.gtwclient.core.display.hud.elements;

import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementMoney extends HudElement {
    public HudElementMoney() {
        super(HudElementType.MONEY, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        float posXLocal = scaledWidth-80 - 15;
        float posYLocal = 75;
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        String money = "$ "+pd.getMoney();
        mc.fontRenderer.drawStringWithShadow(money,
                posXLocal,
                posYLocal,
                0xAEED7A);
    }
}
