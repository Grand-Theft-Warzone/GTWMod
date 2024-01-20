package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.core.minimap.utils.WorldUtils;
import lombok.Getter;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

@Getter
public class Minimap {

    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;

    private int centerX;
    private int centerZ;

    public void update() {
        BlockPos playerPos = Minecraft.getMinecraft().player.getPosition();
        int textRadius = GTWMinimap.getInstance().getTextureSize() / 2;

        minX = playerPos.getX() - textRadius;
        minZ = playerPos.getZ() - textRadius;
        maxX = playerPos.getX() + textRadius;
        maxZ = playerPos.getZ() + textRadius;

        centerX = playerPos.getX();
        centerZ = playerPos.getZ();

        loopTexture(playerPos, textRadius);

        GTWMinimap.getInstance().getMapTexture().update();
    }

    private void loopTexture(BlockPos playerPos, int radius) {
        int textureIndex = 0;

        for (int z = maxZ; z > minZ; z--) {
            for (int x = maxX; x > minX; x--) {
                Chunk chunk = Minecraft.getMinecraft().world.getChunkFromBlockCoords(new BlockPos(x, Minecraft.getMinecraft().player.posY, z));
                if (!chunk.isLoaded()) continue;

                //ChunkManager chunkManager = GTWMinimap.getInstance().getChunkManager();
                //GTWChunk gtwChunk = chunkManager.getChunk(chunk);
                //GTWMinimap.getInstance().getMapTexture().setIndex(textureIndex++, gtwChunk.getMapColor(x, z));

                BlockPos block = WorldUtils.getTopBlock(Minecraft.getMinecraft().world, new BlockPos(x, playerPos.getY() + radius, z));
                IBlockState state = Minecraft.getMinecraft().world.getBlockState(block.down());
                MapColor mapColor = state.getMapColor(Minecraft.getMinecraft().world, block.down());

                GTWMinimap.getInstance().getMapTexture().setIndex(textureIndex++, mapColorToRGB(mapColor));


                if (textureIndex >= GTWMinimap.getInstance().getTextureSize() * GTWMinimap.getInstance().getTextureSize())
                    return;
            }
        }

    }

    public static int mapColorToRGB(MapColor mapColor) {
        int color = mapColor.colorValue;
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = color & 0x000000ff;

        //Set alpha to 255
        return (255 << 24) | (red << 16) | (green << 8) | blue;
    }

}
