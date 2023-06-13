package me.phoenixra.playerhud.hud.elements;

import me.phoenixra.playerhud.data.PlayerData;
import me.phoenixra.playerhud.hud.Hud;
import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementRank extends HudElement {
    public HudElementRank() {
        super(HudElementType.RANK, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        float posXLocal = scaledWidth-40;
        float posYLocal = 41;
        GlStateManager.disableBlend();
        PlayerData pd = Hud.instance.playerData;
        String rank = pd.getRank();
         mc.fontRenderer.drawStringWithShadow(rank,
                posXLocal,
                posYLocal,
                0xAEED7A);

        GlStateManager.enableBlend();
    }
}
