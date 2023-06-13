package me.phoenixra.playerhud.hud.elements;

import me.phoenixra.playerhud.data.PlayerData;
import me.phoenixra.playerhud.hud.Hud;
import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementMoney extends HudElement {
    public HudElementMoney() {
        super(HudElementType.MONEY, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        float posXLocal = scaledWidth-80;
        float posYLocal = 60;
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
