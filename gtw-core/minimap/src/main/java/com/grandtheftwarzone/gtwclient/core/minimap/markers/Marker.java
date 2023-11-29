package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import com.grandtheftwarzone.gtwclient.api.minimap.IMarkerType;
import lombok.Getter;

@Getter
public class Marker {
    private final int posX;
    private final int posZ;
    private final MarkerType type;

    public Marker(int posX, int posY, MarkerType type) {
        this.posX = posX;
        this.posZ = posY;
        this.type = type;
    }

    @Getter
    public enum MarkerType implements IMarkerType {
        //TODO: Add more marker types
        //TODO: Switch from colors to textures
        PLAYER(0xFF0000),
        HOUSE(0x00FF00),
        HOSPITAL(0x0000FF);

        private final int color;

        MarkerType(int color) {
            this.color = color;
        }

        public int getRed() {
            return (getColor() & 0x00ff0000) >> 16;
        }

        public int getBlue() {
            return getColor() & 0x000000ff;
        }

        public int getGreen() {
            return (getColor() & 0x0000ff00) >> 8;
        }
    }

}
