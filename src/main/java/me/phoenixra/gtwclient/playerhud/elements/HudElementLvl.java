package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementLvl extends HudElement {
    public HudElementLvl() {
        super(HudElementType.LEVEL, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
         float posXLocal = scaledWidth-80 - 15;
         float posYLocal = 56;
        GlStateManager.disableBlend();
        PlayerData pd = Hud.instance.playerData;
        String level = "\u272a "+pd.getLevel();
        mc.fontRenderer.drawStringWithShadow(level,
                posXLocal,
                posYLocal,
                COLOR_YELLOW);

        GlStateManager.enableBlend();
    }
}
