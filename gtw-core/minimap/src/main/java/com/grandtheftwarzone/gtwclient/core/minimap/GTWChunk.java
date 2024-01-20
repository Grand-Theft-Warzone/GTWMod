package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.core.minimap.utils.WorldUtils;
import lombok.Getter;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.HashMap;

@Getter
public class GTWChunk {

    private final int x;
    private final int z;
    private final int dimension;

    private final HashMap<Long, Integer> blockColors = new HashMap<>();

    public GTWChunk(Chunk chunk) {
        this.x = chunk.x;
        this.z = chunk.z;
        this.dimension = chunk.getWorld().provider.getDimension();

        World world = chunk.getWorld();

        int startX = chunk.x << 4; // chunk.x * 16
        int startZ = chunk.z << 4; // chunk.z * 16

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BlockPos blockPos = new BlockPos(startX + x, 256, startZ + z);
                BlockPos topBlockPos = WorldUtils.getTopBlock(world, blockPos);

                IBlockState state = world.getBlockState(topBlockPos.down());
                MapColor mapColor = state.getMapColor(world, topBlockPos.down());

                blockColors.put(hash(blockPos.getX(), blockPos.getZ()), Minimap.mapColorToRGB(mapColor));
            }
        }
    }

    public int getMapColor(int x, int z) {
        return blockColors.getOrDefault(hash(x, z), 0xFFFFFF);
    }

    public static long hash(int x, int z) {
        return (((long) x) << 32) | (z & 0xFFFFFFFFL);
    }


}
