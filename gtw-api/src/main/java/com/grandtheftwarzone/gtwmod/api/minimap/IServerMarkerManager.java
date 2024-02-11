package com.grandtheftwarzone.gtwmod.api.minimap;

import java.util.Collection;
import java.util.UUID;

public interface IServerMarkerManager {
    void insert(Marker marker);

    void insert(Collection<Marker> markers);

    void remove(Marker marker);

    void sync();

    void sync(UUID uuid);
}
