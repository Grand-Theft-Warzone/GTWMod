package com.grandtheftwarzone.gtwmod.core.minimap;

import com.grandtheftwarzone.gtwmod.api.minimap.IServerMarkerManager;
import com.grandtheftwarzone.gtwmod.api.minimap.Marker;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
public class ServerMarkerManager implements IServerMarkerManager {
    private final NetworkAPI n;

    private final ArrayList<Marker> markers = new ArrayList<>();

    public void insert(Marker marker) {
        markers.add(marker);
    }

    public void insert(Collection<Marker> markers) {
        markers.addAll(markers);
    }

    public void remove(Marker marker) {
        markers.remove(marker);
    }

    public void sync() {
        n.sendMarkerDataToAll(markers);
    }

    public void sync(UUID uuid) {
        n.sendMarkerData(uuid, markers);
    }

}
