package com.grandtheftwarzone.gtwmod.core.display.minimap;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

@Getter
public class TexturedMinimap {
    private final ResourceLocation texture = new ResourceLocation("gtwclient", "textures/minimap/map_texture.png");
    private final int width;
    private final int height;

    private final long centerX;
    private final long centerZ;

    private final long startX;
    private final long startZ;
    private final long endX;
    private final long endZ;

    @Setter private float zoom;

    public TexturedMinimap(int width, int height, long startX, long startZ, long endX, long endZ, float zoom) {
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startZ = startZ;
        this.endX = endX;
        this.endZ = endZ;
        this.centerX = startX + (endX - startX) / 2;
        this.centerZ = startZ + (endZ - startZ) / 2;
        this.zoom = zoom;
    }
}
