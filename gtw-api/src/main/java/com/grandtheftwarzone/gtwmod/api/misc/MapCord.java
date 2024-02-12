package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapCord {

    private double x, y;


    public MapCord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MapCord() {
        this.x = 0;
        this.y = 0;
    }
}
