package com.grandtheftwarzone.gtwmod.api.map.manager.client;

import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;

import java.util.List;

public interface MarkerManagerClient {

    List<MapMarker> getLocalMarkerList();

    List<MapMarker> getAllMarkerFilter(String mapImageId);
    List<MapMarker> getAllMarker();

    MapMarker removeLocalMarker(String id);

    void addLocalMarker(MapMarker marker);

    void addLocalMarker(MapMarker marker, boolean save);

    void updateServerMarkers(List<TemplateMarker> markers);


}
