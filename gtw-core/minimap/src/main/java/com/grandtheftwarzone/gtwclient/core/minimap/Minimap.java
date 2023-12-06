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

    private double startX;
    private double startZ;
    private double endX;
    private double endZ;

    private double playerX;
    private double playerZ;

    public void update() {
//        BlockPos playerPos = Minecraft.getMinecraft().player.getPosition();
        EntityPlayer player = Minecraft.getMinecraft().player;
        int radius = GTWMinimap.getInstance().getRadius();

        playerX = player.posX;
        playerZ = player.posZ;

        startX = player.posX - radius;
        startZ = player.posZ - radius;

        endX = player.posX + radius;
        endZ = player.posZ + radius;

        int textureIndex = 0;

        for (double x = endX; x > startX; x--) { // For some reason, the map is flipped in the x axis
            for (double z = startZ; z < endZ; z++) {
                Chunk chunk = Minecraft.getMinecraft().world.getChunkFromBlockCoords(new BlockPos(x, player.posY, z));
                if (!chunk.isLoaded()) continue;

                //TODO: Cache chunks

                BlockPos block = WorldUtils.getTopBlock(Minecraft.getMinecraft().world, new BlockPos(x, player.posY, z));
                IBlockState state = Minecraft.getMinecraft().world.getBlockState(block.down());
                MapColor mapColor = state.getMapColor(Minecraft.getMinecraft().world, block.down());

                GTWMinimap.getInstance().getMapTexture().setIndex(textureIndex++, mapColorToRGB(mapColor));
            }
        }

        GTWMinimap.getInstance().getMapTexture().update();
    }

    public int mapColorToRGB(MapColor mapColor) {
        int color = mapColor.colorValue;
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = color & 0x000000ff;

        //Set alpha to 255
        return (255 << 24) | (red << 16) | (green << 8) | blue;
    }

}
