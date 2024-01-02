package com.grandtheftwarzone.gtwmod.core.emoji.gui;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.List;


/**
 * Utilities for rendering items.
 * @author rubensworks
 */
public class ItemRenderUtil {

    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
    public static int SLOT_SIZE = 18;
    public static int SLOT_SIZE_INNER = 16;

    public static Triple<Float, Float, Float> intToRGB(int color) {
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        return Triple.of(red, green, blue);
    }

    public static TextureAtlasSprite getFluidIcon(FluidStack fluid, EnumFacing side) {
        Block defaultBlock = Blocks.WATER;
        Block block = defaultBlock;

        if (fluid.getFluid().getBlock() != null) {
            block = fluid.getFluid().getBlock();
        }

        if (side == null) {
            side = EnumFacing.UP;
        }

        TextureAtlasSprite icon = TEXTURE_GETTER.apply(fluid.getFluid().getFlowing(fluid));
        if (icon == null || side == EnumFacing.UP || side == EnumFacing.DOWN) {
            icon = TEXTURE_GETTER.apply(fluid.getFluid().getStill(fluid));
        }

        if (icon == null) {
            icon = getBlockIcon(block);
            if (icon == null) {
                icon = getBlockIcon(defaultBlock);
            }
        }

        return icon;
    }


    private static TextureAtlasSprite getBlockIcon(Block block) {
        IBlockState state = block.getDefaultState();
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);

        if (model != null) {
            List<BakedQuad> quads = model.getQuads(state, null, 0);
            if (!quads.isEmpty()) {
                return quads.get(0).getSprite();
            }
        }

        return null;
    }

    public static void renderFluidTank(Gui gui, @Nullable FluidStack fluidStack, int capacity, int x, int y, int width, int height) {
        if (fluidStack != null && capacity > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();  // Добавлено

            int level = (int) ((double) height * ((double) fluidStack.amount / (double) capacity));
            TextureAtlasSprite icon = getFluidIcon(fluidStack, EnumFacing.UP);

            for (int verticalOffset = 0; level > 0; verticalOffset += 16) {
                int textureHeight;
                if (level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);  // Заменено на LOCATION_BLOCKS_TEXTURE
                Triple<Float, Float, Float> colorParts = intToRGB(fluidStack.getFluid().getColor(fluidStack));
                GlStateManager.color(colorParts.getLeft(), colorParts.getMiddle(), colorParts.getRight(), 1.0F);
                gui.drawTexturedModalRect(x, y - textureHeight - verticalOffset + height, icon, width, textureHeight);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }


    public static void renderFluidSlot(Gui gui, @Nullable FluidStack fluidStack, int x, int y) {
        if (fluidStack != null) {
            renderFluidTank(gui, fluidStack, fluidStack.amount, x, y, SLOT_SIZE_INNER, SLOT_SIZE_INNER);
        }
    }





    public static void renderItem(ItemStack itemStack, int scale) {
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.0625, 0.0625, 0.01);
        ItemLightingUtil.enableGUIStandardItemLighting(scale);

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(40f, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(95F, 1.0F, 0.0F, 0.0F);
        GlStateManager.popMatrix();

        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -1);

        GlStateManager.pushAttrib();
        GlStateManager.enableRescaleNormal();
        GlStateManager.popAttrib();

        renderItem.renderItemAndEffectIntoGUI(itemStack, 0, 0);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();

        GlStateManager.disablePolygonOffset();

        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    public static void renderFluid(Gui gui, Fluid fluid, int scale) {
        GlStateManager.scale(scale / 16, scale / 16, scale / 16);
        renderFluidSlot(gui, new FluidStack(fluid, Fluid.BUCKET_VOLUME), 0, 0);
    }

}
