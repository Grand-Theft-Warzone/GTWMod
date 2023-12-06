package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import com.grandtheftwarzone.gtwclient.core.minimap.utils.TextureAtlas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
@Getter
public class MarkerType {
    public static final ResourceLocation DEFAULT_MARKERS_TEXTURE = new ResourceLocation("gtwclient", "textures/minimap/markers_25.png");
    public static final int DEFAULT_WIDTH = 64 / 4;
    public static final int DEFAULT_HEIGHT = 64 / 4;
    public static final int DEFAULT_TEXTURE_WIDTH = 256 / 4;
    public static final int DEFAULT_TEXTURE_HEIGHT = 256 / 4;

    public static final MarkerType PLAYER = defaultGlobalMarker(0);
    public static final MarkerType HOME = defaultGlobalMarker(0);
    public static final MarkerType GARAGE = defaultGlobalMarker(7);
    public static final MarkerType CLOTHES_SHOP = defaultGlobalMarker(4);
    public static final MarkerType WEAPONS_SHOP = defaultGlobalMarker(6);
    public static final MarkerType MISSION = defaultGlobalMarker(3);
    public static final MarkerType FACTORY = defaultGlobalMarker(1);

    private static MarkerType defaultGlobalMarker(int textureIndex) {
        TextureAtlas atlas = new TextureAtlas(textureIndex, DEFAULT_TEXTURE_WIDTH, DEFAULT_TEXTURE_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        return new MarkerType(DEFAULT_MARKERS_TEXTURE, atlas, true);
    }

    private final ResourceLocation texture;
    private final TextureAtlas atlas;
    private final boolean global;
}
