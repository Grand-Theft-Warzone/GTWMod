package me.phoenixra.playerhud.hud.elements;

import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.hud.HudElementType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;

public class HudElementHealth extends HudElement {
    public HudElementHealth() {
        super(HudElementType.HEALTH, 0, 0, 0, 0);
    }
    @Override
    public void drawElement(Gui gui,
                            float zLevel,
                            float partialTicks,
                            int scaledWidth,
                            int scaledHeight) {
        int posXLocal = scaledWidth - 80;
        int posYLocal = 10;
        EntityPlayerSP player = mc.player;
        int health = MathHelper.ceil(this.mc.player.getHealth());
        int absorption = MathHelper.ceil(this.mc.player.getAbsorptionAmount());
        int healthMax = MathHelper.ceil(this.mc.player.getMaxHealth());

        GlStateManager.disableLighting();
        if(absorption > 1)
            drawCustomBar(posXLocal,
                    posYLocal,
                    80,
                    8,
                    (double) (health + absorption) / (double) (healthMax + absorption) * 100D,
                    COLOR_ORANGE,
                    COLOR_ORANGE,
                    true
            );

        if(player.isPotionActive(MobEffects.POISON)) {
            drawCustomBar(posXLocal,
                    posYLocal,
                    80,
                    8,
                    (double) health / (double) (healthMax + absorption) * 100D,
                    COLOR_PURPLE,
                    COLOR_PURPLE,
                    true
            );
        } else if(player.isPotionActive(MobEffects.WITHER)) {
            drawCustomBar(posXLocal,
                    posYLocal,
                    80,
                    8,
                    (double) health / (double) (healthMax + absorption) * 100D,
                    COLOR_BLACK,
                    COLOR_BLACK,
                    true
            );
        } else {
            drawCustomBar(posXLocal,
                    posYLocal,
                    80,
                    8,
                    (double) health / (double) (healthMax + absorption) * 100D,
                    COLOR_RED,
                    COLOR_RED,
                    true
            );
        }

    }
}
