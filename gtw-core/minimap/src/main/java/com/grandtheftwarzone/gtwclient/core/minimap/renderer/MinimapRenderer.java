package com.grandtheftwarzone.gtwclient.core.minimap.renderer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class MinimapRenderer {

    private static final int TEXTURE_WIDTH = /*128*/64;
    private static final int TEXTURE_HEIGHT = /*128*/64;
    private static final int TEXTURE_SIZE = TEXTURE_WIDTH * TEXTURE_HEIGHT;

    private final byte[] miniMapColors = new byte[TEXTURE_SIZE];
    private final DynamicTexture miniMapTexture;

    private final static ResourceLocation MAP_ICONS = new ResourceLocation("minecraft", "textures/map/map_icons.png");

    public MinimapRenderer() {
        miniMapTexture = new DynamicTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT);

        Arrays.fill(miniMapTexture.getTextureData(), 0);
    }

    public void renderMiniMap(int x, int y, int radius) {
        float rotation = Minecraft.getMinecraft().player.rotationYaw;

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        //TODO: Render Overlay

        GlStateManager.bindTexture(miniMapTexture.getGlTextureId());
        drawCircle(x, y, radius, rotation);

        drawPlayerMarker(x, y);

        //TODO: Render markers

        GlStateManager.popMatrix();
    }

    private void drawPlayerMarker(int x, int y) {
        // Draw player marker using minecrafts map icons
        Minecraft.getMinecraft().getTextureManager().bindTexture(MAP_ICONS);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        double iconSize = 0.25;
        buffer.pos(x - 7, y - 7, 0).tex(0, 0).endVertex();
        buffer.pos(x - 7, y + 7, 0).tex(0, iconSize).endVertex();
        buffer.pos(x + 7, y + 7, 0).tex(iconSize, iconSize).endVertex();
        buffer.pos(x + 7, y - 7, 0).tex(iconSize, 0).endVertex();

        tessellator.draw();
    }

    private void drawCircle(int x, int y, float radius, float rotation) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(rotation, 0, 0, 1);

        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 360; i >= 0; i -= 5) {
            GL11.glTexCoord2f((MathHelper.cos((float) Math.toRadians(i)) + 1) * 0.5f, (MathHelper.sin((float) Math.toRadians(i)) + 1) * 0.5f);
            GL11.glVertex2f(MathHelper.cos((float) Math.toRadians(i)) * radius, MathHelper.sin((float) Math.toRadians(i)) * radius);
        }

        GL11.glEnd();
        GlStateManager.popMatrix();
    }

    public void updateMapData(World worldIn) {
        int i = 1;
        int j = Minecraft.getMinecraft().player.getPosition().getX();
        int k = Minecraft.getMinecraft().player.getPosition().getZ();
        int l = MathHelper.floor(Minecraft.getMinecraft().player.posX - (double) j) / i + (TEXTURE_WIDTH / 2);
        int i1 = MathHelper.floor(Minecraft.getMinecraft().player.posZ - (double) k) / i + (TEXTURE_HEIGHT / 2);
        int j1 = TEXTURE_SIZE / i;

        if (!worldIn.provider.hasSkyLight()) {
            j1 /= 2;
        }

        for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
            double d0 = 0.0D;

            for (int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                if (k1 >= 0 && l1 >= -1 && k1 < TEXTURE_WIDTH && l1 < TEXTURE_HEIGHT) {
                    int i2 = k1 - l;
                    int j2 = l1 - i1;
                    boolean flag1 = i2 * i2 + j2 * j2 > (j1 - 2) * (j1 - 2);
                    int k2 = (j / i + k1 - (TEXTURE_WIDTH / 2)) * i;
                    int l2 = (k / i + l1 - (TEXTURE_HEIGHT / 2)) * i;

                    Multiset<MapColor> multiset = HashMultiset.create();
                    Chunk chunk = worldIn.getChunkFromBlockCoords(new BlockPos(k2, 0, l2));

                    if (!chunk.isEmpty()) {
                        int i3 = k2 & 15;
                        int j3 = l2 & 15;
                        int k3 = 0;
                        double d1 = 0.0D;

                        if (!worldIn.provider.hasSkyLight()) {
                            int l3 = k2 + l2 * 231871;
                            l3 = l3 * l3 * 31287121 + l3 * 11;

                            //TODO: Check this
                            if ((l3 >> 20 & 1) == 0) {
//                                multiset.add(Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT)), 10);
                                multiset.add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMaterial().getMaterialMapColor());
                            } else {
                                multiset.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMaterial().getMaterialMapColor());
//                                multiset.add(Blocks.stone.getMapColor(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)), 100);
                            }

                            d1 = 100.0D;
                        } else {
                            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                            for (int i4 = 0; i4 < i; ++i4) {
                                for (int j4 = 0; j4 < i; ++j4) {
                                    int k4 = chunk.getHeightValue(i4 + i3, j4 + j3) + 1;
                                    IBlockState iblockstate = Blocks.AIR.getDefaultState();

                                    if (k4 > 1) {
                                        label541:
                                        {
                                            while (true) {
                                                --k4;
                                                iblockstate = chunk.getBlockState(blockpos$mutableblockpos.setPos(i4 + i3, k4, j4 + j3));

                                                if (iblockstate.getBlock().getDefaultState().getMaterial().getMaterialMapColor() != MapColor.AIR || k4 <= 0) {
                                                    break;
                                                }
                                            }

                                            if (k4 > 0 && iblockstate.getBlock().getDefaultState().getMaterial().isLiquid()) {
                                                int l4 = k4 - 1;

                                                while (true) {
                                                    IBlockState block = chunk.getBlockState(i4 + i3, l4--, j4 + j3);
                                                    ++k3;

                                                    if (l4 <= 0 || !block.getMaterial().isLiquid()) {
                                                        break label541;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    d1 += (double) k4 / (double) (i * i);
                                    multiset.add(iblockstate.getBlock().getDefaultState().getMaterial().getMaterialMapColor());
                                }
                            }
                        }

                        k3 = k3 / (i * i);
                        double d2 = (d1 - d0) * 4.0D / (double) (i + 4) + ((double) (k1 + l1 & 1) - 0.5D) * 0.4D;
                        int i5 = 1;

                        if (d2 > 0.6D) {
                            i5 = 2;
                        }

                        if (d2 < -0.6D) {
                            i5 = 0;
                        }

                        MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.AIR);

                        if (mapcolor == MapColor.WATER) {
                            d2 = (double) k3 * 0.1D + (double) (k1 + l1 & 1) * 0.2D;
                            i5 = 1;

                            if (d2 < 0.5D) {
                                i5 = 2;
                            }

                            if (d2 > 0.9D) {
                                i5 = 0;
                            }
                        }

                        d0 = d1;

                        if (l1 >= 0 && i2 * i2 + j2 * j2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
                            byte b0 = miniMapColors[k1 + l1 * (TEXTURE_WIDTH)];
                            byte b1 = (byte) (mapcolor.colorIndex * 4 + i5);

                            if (b0 != b1) {
                                miniMapColors[k1 + l1 * (TEXTURE_WIDTH)] = b1;
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateMapTexture() {
        for (int i = 0; i < TEXTURE_SIZE; i++) {
            int j = miniMapColors[i] & 255;

            if (j / 4 == 0) miniMapTexture.getTextureData()[i] = (i + i / TEXTURE_WIDTH & 1) * 8 + 16 << 24;
            else miniMapTexture.getTextureData()[i] = MapColor.COLORS[j / 4].getMapColor(j & 3);
        }

        try {
            miniMapTexture.updateDynamicTexture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
