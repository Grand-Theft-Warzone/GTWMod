package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Marker {
    private final int posX;
    private final int posZ;
    private final MarkerType type;

    public int getHash() {
        return getHash(posX, posZ);
    }

    public static int getHash(double x, double z) {
        return (int) (x * 31 + z * 17); //TODO: Improve hash
    }
}
