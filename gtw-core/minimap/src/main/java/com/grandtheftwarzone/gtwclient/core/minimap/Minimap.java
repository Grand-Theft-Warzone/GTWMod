package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.core.minimap.utils.WorldUtils;
import lombok.Getter;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class Minimap {

    @Getter private int startX, startZ, endX, endZ;

    public void update() {
        BlockPos playerPos = Minecraft.getMinecraft().player.getPosition();
        int radius = GTWMinimap.getInstance().getRadius();

        startX = playerPos.getX() - radius;
        startZ = playerPos.getZ() - radius;

        endX = playerPos.getX() + radius;
        endZ = playerPos.getZ() + radius;

        int textureIndex = 0;

        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                Chunk chunk = Minecraft.getMinecraft().world.getChunkFromBlockCoords(new BlockPos(x, playerPos.getY(), z));
                if (!chunk.isLoaded()) continue;

                //TODO: Cache chunks

                BlockPos block = WorldUtils.getTopBlock(Minecraft.getMinecraft().world, new BlockPos(x, playerPos.getY(), z));
                IBlockState state = Minecraft.getMinecraft().world.getBlockState(block.down());
                MapColor mapColor = state.getMapColor(Minecraft.getMinecraft().world, block.down());

                //Get RBG values from MapColor
                int color = mapColor.colorValue;
                int red = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue = color & 0x000000ff;

                //Set alpha to 255
                color = (255 << 24) | (red << 16) | (green << 8) | blue;

                //Get correct color taking in account biome and etc

                GTWMinimap.getInstance().getMapTexture().setIndex(textureIndex++, color);
            }
        }

        GTWMinimap.getInstance().getMapTexture().update();
    }

}
