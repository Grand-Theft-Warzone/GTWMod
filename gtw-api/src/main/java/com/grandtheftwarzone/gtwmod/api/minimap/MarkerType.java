package com.grandtheftwarzone.gtwmod.api.minimap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
@Getter
public class MarkerType {
    public static final ResourceLocation DEFAULT_MARKERS_TEXTURE = new ResourceLocation("gtwclient", "textures/minimap/markers.png");
    public static final int DEFAULT_WIDTH = 12;
    public static final int DEFAULT_HEIGHT = 12;
    public static final int DEFAULT_TEXTURE_WIDTH = 256;
    public static final int DEFAULT_TEXTURE_HEIGHT = 256;

    public static final MarkerType PLAYER = defaultGlobalMarker(0);
    public static final MarkerType HOME = defaultGlobalMarker(7);
    public static final MarkerType GARAGE = defaultGlobalMarker(1);
    public static final MarkerType CLOTHES_SHOP = defaultGlobalMarker(3);
    public static final MarkerType WEAPONS_SHOP = defaultGlobalMarker(5);
    public static final MarkerType MISSION = defaultGlobalMarker(4);
    public static final MarkerType FACTORY = defaultGlobalMarker(6);

    private final ResourceLocation texture;
    private final TextureAtlas atlas;
    private final boolean global;

    private static MarkerType defaultGlobalMarker(int textureIndex) {
        TextureAtlas atlas = new TextureAtlas(textureIndex, DEFAULT_TEXTURE_WIDTH, DEFAULT_TEXTURE_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        return new MarkerType(DEFAULT_MARKERS_TEXTURE, atlas, true);
    }
}
