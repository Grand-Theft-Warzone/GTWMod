package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementFood extends HudElement {
    public HudElementFood() {
        super(HudElementType.FOOD, 0, 0, 0, 0);
    }

    @Override
    public void drawElement(Gui gui,
                            float zLevel,
                            float partialTicks,
                            int scaledWidth,
                            int scaledHeight) {
        int posXLocal = scaledWidth - 80;
        int posYLocal = 23;
        EntityPlayerSP player = mc.player;
        int stamina = player.getFoodStats().getFoodLevel();
        int staminaMax = 20;

        GlStateManager.disableLighting();
        drawCustomBar(posXLocal,
                posYLocal,
                80,
                8,
                stamina / (double) staminaMax * 100.0D,
                COLOR_BROWN,
                COLOR_BROWN,
                true
        );

    }
}
