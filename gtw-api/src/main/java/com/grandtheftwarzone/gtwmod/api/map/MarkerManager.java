package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;

import java.util.List;

public interface MarkerManager {

    List<MapMarker> getLocalMarkerList();

    List<MapMarker> getAllMarkerFilter(String mapImageId);
    List<MapMarker> getAllMarker();

    MapMarker removeLocalMarker(String id);

    void addLocalMarker(MapMarker marker);

    void addLocalMarker(MapMarker marker, boolean save);

}
