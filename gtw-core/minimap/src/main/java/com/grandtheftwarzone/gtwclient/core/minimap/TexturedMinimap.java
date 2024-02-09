package com.grandtheftwarzone.gtwclient.core.minimap;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

@Getter
public class TexturedMinimap {

    private final ResourceLocation texture = new ResourceLocation("gtwclient", "textures/minimap/map_texture.png");
    private final int width;
    private final int height;

    private final int centerX;
    private final int centerZ;

    private final int startX;
    private final int startZ;
    private final int endX;
    private final int endZ;

    @Setter private float zoom;

    public TexturedMinimap(int width, int height, int startX, int startZ, int endX, int endZ, float zoom) {
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
