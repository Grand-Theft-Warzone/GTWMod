package com.grandtheftwarzone.gtwclient.core.minimap;

import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;

public class ChunkManager {

    private final HashMap<Integer, GTWChunk> chunks = new HashMap<>();

    public void addChunk(Chunk chunk) {
        chunks.put(hashChunk(chunk), new GTWChunk(chunk));
    }

    public boolean hasChunk(Chunk chunk) {
        return chunks.containsKey(hashChunk(chunk));
    }

    public GTWChunk getChunk(Chunk chunk) {
        return chunks.get(hashChunk(chunk));
    }

    public void removeChunk(Chunk chunk) {
        chunks.remove(hashChunk(chunk));
    }

    public static int hashGTWChunk(GTWChunk chunk) {
        return (chunk.getX() << 8) + (chunk.getZ() << 8) + chunk.getDimension();
    }

    public static int hashChunk(Chunk chunk) {
        return (chunk.x << 8) + (chunk.z << 8) + chunk.getWorld().provider.getDimension();
    }
}
