package com.grandtheftwarzone.gtwclient.core.display.hud.elements;

import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class HudElementWidget extends HudElement {
    public HudElementWidget() {
        super(HudElementType.WIDGET, 0, 0, 0, 0);
    }

    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        int posXLocal = scaledWidth-125 - 15;
        int posYLocal = 10;
        EntityPlayer player = mc.player;

        ItemStack heldItemStack = player.getHeldItemMainhand();
        if (!heldItemStack.isEmpty()) {
            // Get the item's icon texture
            try {
                ResourceLocation key = ForgeRegistries.ITEMS.getKey(heldItemStack.getItem());
                float scaleX = 60/2.0f;
                float scaleY = 60/2.0f;
                renderItemIntoGUI(heldItemStack,
                        posXLocal+13,
                        posYLocal+20,
                        scaleX,scaleY
                );
            }catch (Throwable e){
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
