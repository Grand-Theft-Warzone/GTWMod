package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
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
                0xAEED7A
         );

        GlStateManager.enableBlend();
    }
}
