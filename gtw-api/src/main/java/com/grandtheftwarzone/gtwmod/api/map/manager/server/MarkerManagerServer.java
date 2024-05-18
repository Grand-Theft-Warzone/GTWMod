package com.grandtheftwarzone.gtwmod.api.map.manager.server;

import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface MarkerManagerServer {

    @Nullable
    ServerMarker removeMarker(String identificator);

    void createOrUpdateMarker(ServerMarker serverMarker);

    @Nullable ServerMarker getMarker(String identificator);

    @Nullable List<ServerMarker> getMarkerFilterUUID(UUID uuid);

    List<ServerMarker> getAllMarker();

    void initMarker();

}
