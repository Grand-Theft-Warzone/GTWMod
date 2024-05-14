package com.grandtheftwarzone.gtwmod.core.map.database;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.manager.server.MarkerManagerServer;
import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;
import lombok.Getter;
import me.phoenixra.atumconfig.api.utils.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GtwServerMarkerManager implements MarkerManagerServer {

    @Getter
    private StorageManager storageManager;

    private List<ServerMarker> serverMarkerList = new ArrayList<>();

    public GtwServerMarkerManager(StorageManager storageManager) {
        this.storageManager = storageManager;
        initMarker();
    }

    public void initMarker() {
        List<ServerMarker> dbServerMarkers = storageManager.getAllMarkers();

        serverMarkerList = dbServerMarkers;
        GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&e[GTWMap] &aMarkers loaded. " + dbServerMarkers.size() + " markers are displayed."));
    }

    public @Nullable ServerMarker removeMarker(String identificator) {

        ServerMarker removedMarker = getMarker(identificator);

        if (removedMarker!= null) {
            storageManager.removeMarkerFromId(removedMarker.getIdentificator());
            serverMarkerList.remove(removedMarker);
        }

        return removedMarker;
    }


    /**
     * Changes the marker data or creates it if it does not exist.
     * @param serverMarker marker object.
     * @return false - the marker did not previously exist. true - the marker previously existed.
     */
    public boolean createOrUpdateMarker(ServerMarker serverMarker) {
        String updateServerMarkerId = serverMarker.getIdentificator();
        ServerMarker oldServerMarker = getMarker(updateServerMarkerId);
        if (oldServerMarker == null) {
            storageManager.createOrUpdateMarker(serverMarker);
            serverMarkerList.add(serverMarker);
            return false;
        }

        oldServerMarker.updateData(serverMarker);
        storageManager.createOrUpdateMarker(serverMarker);
        return true;
    }

    public @Nullable ServerMarker getMarker(String identificator) {
        if (!serverMarkerList.isEmpty()) {
            for (ServerMarker marker : serverMarkerList) {
                if (marker.getIdentificator().equals(identificator)) {
                    return marker;
                }
            }
        }
        return null;
    }

    public List<ServerMarker> getAllMarker() {
        return new ArrayList<>(serverMarkerList);

    }

}
