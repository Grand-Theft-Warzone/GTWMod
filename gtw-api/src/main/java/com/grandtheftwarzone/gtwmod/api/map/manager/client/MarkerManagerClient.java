package com.grandtheftwarzone.gtwmod.api.map.manager.client;

import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.PlayerMarker;

import javax.annotation.Nullable;
import java.util.List;

public interface MarkerManagerClient {

    List<MapMarker> getLocalMarkerList();

    List<MapMarker> getAllMarkerFilter(String mapImageId);
    List<MapMarker> getAllMarker();

    MapMarker getServerMarker(String identificator);

    MapMarker removeLocalMarker(String id);

    void addLocalMarker(MapMarker marker);

    void addLocalMarker(MapMarker marker, boolean save);

    void updateServerMarkers(List<TemplateMarker> markers);

    @Nullable
    PlayerMarker getPlayerMarker();


}
