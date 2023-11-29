package com.grandtheftwarzone.gtwclient.api.minimap;

import java.util.ArrayList;

public interface MarkerManager {
    void updateMarkers(ArrayList<IMaker> markers);
    void syncMarkers();
}
