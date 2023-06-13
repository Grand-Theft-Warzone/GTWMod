package me.phoenixra.playerhud.hud.elements;

import ibxm.Player;
import me.phoenixra.playerhud.PlayerHUD;
import me.phoenixra.playerhud.data.PlayerData;
import me.phoenixra.playerhud.hud.Hud;
import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.hud.HudElementType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class HudElementExperience extends HudElement {
    public HudElementExperience() {
        super(HudElementType.EXPERIENCE, 0, 0, 0, 0);
    }
    @Override
    public void drawElement(Gui gui,
                            float zLevel,
                            float partialTicks,
                            int scaledWidth,
                            int scaledHeight) {
         int posXLocal = scaledWidth - 80;
         int posYLocal = 50;
        PlayerData pd = Hud.instance.playerData;
        double exp = pd.getExperience();
        double expCap = pd.getExperienceCap();
        double full = 100D / expCap;
        GlStateManager.disableLighting();
        drawCustomBar(posXLocal,
                 posYLocal,
                80,
                8,
                exp * full,
                0xAEED7A,
                0xAEED7A,
                true
        );

    }
}
