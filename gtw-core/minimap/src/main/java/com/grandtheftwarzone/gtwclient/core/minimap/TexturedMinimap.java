package com.grandtheftwarzone.gtwclient.core.minimap;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

@Getter
public class TexturedMinimap {

    private final ResourceLocation texture = new ResourceLocation("gtwclient", "textures/minimap/texture.png");
    private final int width;
    private final int height;

    private final int centerX;
    private final int centerZ;

    @Setter private float zoom;

    public TexturedMinimap(int width, int height, float zoom) {
        this.width = width;
        this.height = height;
        this.centerX = 0;
        this.centerZ = 0;
        this.zoom = zoom;
    }
}