package me.phoenixra.playerhud.hud.elements;

import me.phoenixra.playerhud.PlayerHUD;
import me.phoenixra.playerhud.data.PlayerData;
import me.phoenixra.playerhud.hud.Hud;
import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementLvl extends HudElement {
    public HudElementLvl() {
        super(HudElementType.LEVEL, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
         float posXLocal = scaledWidth-80;
         float posYLocal = 41;
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
