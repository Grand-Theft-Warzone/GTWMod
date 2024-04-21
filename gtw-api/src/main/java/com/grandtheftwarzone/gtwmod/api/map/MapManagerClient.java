package com.grandtheftwarzone.gtwmod.api.map;


import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersClient;

public interface MapManagerClient {
    MapConsumersClient getMapConsumers();

    MinimapManager getMinimapManager();
    GlobalmapManager getGlobalmapManager();

    MapImageUtils getMapImageUtils();

    // ------------------------------------------

    // @TODO: remove, было для дебага
    boolean isAllowedToDisplay();
    void setAllowedToDisplay(Boolean draw, Boolean quietChange);

}
