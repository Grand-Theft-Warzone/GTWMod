package com.grandtheftwarzone.gtwmod.api.map;


import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersClient;

public interface MapManagerClient {
    MapConsumersClient getMapConsumers();

    MinimapManager getMinimapManager();
    GlobalmapManager getGlobalmapManager();

}
