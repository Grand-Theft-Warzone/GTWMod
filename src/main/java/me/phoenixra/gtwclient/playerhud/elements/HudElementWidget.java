package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class HudElementWidget extends HudElement {
    public HudElementWidget() {
        super(HudElementType.WIDGET, 0, 0, 0, 0);
    }


    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        int posXLocal = scaledWidth-125;
        int posYLocal = 10;
        EntityPlayer player = mc.player;

        ItemStack heldItemStack = player.getHeldItemMainhand();
        if (!heldItemStack.isEmpty()) {
            // Get the item's icon texture
            try {
                TextureAtlasSprite icon = mc.getRenderItem().getItemModelWithOverrides(heldItemStack,
                        null,
                        null).getParticleTexture();
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.ingameGUI.drawTexturedModalRect(
                        posXLocal + 6,
                        posYLocal + 6,
                        icon, 30, 39);
            }catch (Exception e){
                Tessellator tessellator = Tessellator.getInstance();
                tessellator.getBuffer().finishDrawing();
                bind(FIST_ICON);
                gui.drawTexturedModalRect(
                        posXLocal,
                        posYLocal,
                        0,
                        0,
                        53,
                        68
                );
                return;
            }

            drawRect(posXLocal,
                    posYLocal,
                    39,
                    3,
                    COLOR_BLACK);
            drawRect(posXLocal+39,
                    posYLocal,
                    3,
                    48,
                    COLOR_BLACK);
            drawRect(posXLocal,
                    posYLocal,
                    3,
                    48,
                    COLOR_BLACK);
            drawRect(posXLocal,
                    posYLocal+48,
                    42,
                    3,
                    COLOR_BLACK);

        }else {
            bind(FIST_ICON);
            gui.drawTexturedModalRect(
                    posXLocal,
                    posYLocal,
                    0,
                    0,
                    53,
                    68
            );
        }
    }
}
