package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapCord {

    // z - height axis
    private double x, y, z;


    public MapCord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MapCord(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MapCord() {
        this.x = 0;
        this.y = 0;
    }
}
