package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementMoney extends HudElement {
    public HudElementMoney() {
        super(HudElementType.MONEY, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        float posXLocal = scaledWidth-80 - 15;
        float posYLocal = 75;
        GlStateManager.disableBlend();
        PlayerData pd = Hud.instance.playerData;
        String money = "$ "+pd.getMoney();
        mc.fontRenderer.drawStringWithShadow(money,
                posXLocal,
                posYLocal,
                0xAEED7A);

        GlStateManager.enableBlend();
    }
}
