package com.grandtheftwarzone.gtwmod.core.display.minimap.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldUtils {
    public static BlockPos getTopBlock(World world, BlockPos startingPos) {
        Chunk chunk = world.getChunkFromBlockCoords(startingPos);
        BlockPos posA;
        BlockPos posB;

        for (posA = new BlockPos(startingPos.getX(), chunk.getTopFilledSegment() + 16, startingPos.getZ());
             posA.getY() >= 0; posA = posB) {
            posB = posA.down();
            IBlockState block = chunk.getBlockState(posB);

            if (block.getMaterial().blocksMovement() || block.getMaterial().isLiquid()) {
                break;
            }
        }

        return posA;
    }
}
