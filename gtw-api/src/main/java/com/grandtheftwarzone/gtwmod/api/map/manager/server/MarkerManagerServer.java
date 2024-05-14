package com.grandtheftwarzone.gtwmod.api.map.manager.server;

import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;

import javax.annotation.Nullable;
import java.util.List;

public interface MarkerManagerServer {

    @Nullable
    ServerMarker removeMarker(String identificator);

    /**
     * Changes the marker data or creates it if it does not exist.
     * @param serverMarker marker object.
     * @return false - the marker did not previously exist. true - the marker previously existed.
     */
    boolean createOrUpdateMarker(ServerMarker serverMarker);

    @Nullable ServerMarker getMarker(String identificator);

    List<ServerMarker> getAllMarker();

}
