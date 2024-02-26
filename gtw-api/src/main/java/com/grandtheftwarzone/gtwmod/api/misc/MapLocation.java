package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MapLocation {

    // z - height axis
    private double x, y, z;


    public MapLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MapLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MapLocation(String str) {
        String[] cord = str.split(";");
        this.x = Double.parseDouble(cord[0]);
        this.y = Double.parseDouble(cord[1]);
        this.z = Double.parseDouble(cord[2]);
    }

    public String toString() {
        return x + ";" + y + ";" + z;
    }

    public MapLocation() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
}
