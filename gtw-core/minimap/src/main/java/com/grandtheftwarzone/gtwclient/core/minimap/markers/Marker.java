package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import lombok.Getter;

public class Marker {

    @Getter private final int posX;
    @Getter private final int posY;

    public Marker(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
