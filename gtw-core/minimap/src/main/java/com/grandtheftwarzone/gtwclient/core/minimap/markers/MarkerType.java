package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
@Getter
public class MarkerType {
    public static final ResourceLocation MARKERS_TEXTURE = new ResourceLocation("gtwcore", "textures/gui/markers.png");

    public static final MarkerType PLAYER = new MarkerType(MARKERS_TEXTURE, true, 0, 0, 8, 8);
    public static final MarkerType HOME = new MarkerType(MARKERS_TEXTURE, true, 8, 0, 8, 8);
    public static final MarkerType GARAGE = new MarkerType(MARKERS_TEXTURE, true, 16, 0, 8, 8);
    public static final MarkerType CLOTHES_SHOP = new MarkerType(MARKERS_TEXTURE, true, 24, 0, 8, 8);
    public static final MarkerType WEAPONS_SHOP = new MarkerType(MARKERS_TEXTURE, true, 32, 0, 8, 8);
    public static final MarkerType MISSION = new MarkerType(MARKERS_TEXTURE, true, 40, 0, 8, 8);
    public static final MarkerType FACTORY = new MarkerType(MARKERS_TEXTURE, true, 48, 0, 8, 8);

    private final ResourceLocation texture;
    private final boolean global;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;
}
