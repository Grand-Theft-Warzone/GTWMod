package com.grandtheftwarzone.gtwmod.api.minimap;

import com.grandtheftwarzone.gtwmod.api.minimap.Marker;

import java.util.ArrayList;
import java.util.List;

public interface IClientMarkerManager {

    void syncMarkers(List<Marker> newMarkers);

    ArrayList<Marker> getMarkers(double startX, double startZ, double endX, double endZ);

    boolean hasMarkerOverlap(Marker marker);
}
