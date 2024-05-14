package com.grandtheftwarzone.gtwmod.api.map.manager.client;


import com.grandtheftwarzone.gtwmod.api.map.MapImageUtils;
import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersClient;
import com.grandtheftwarzone.gtwmod.api.map.marker.RadarPlayer;
import net.minecraft.client.settings.KeyBinding;

public interface MapManagerClient {
    MapConsumersClient getMapConsumers();
    MinimapManager getMinimapManager();
    GlobalmapManager getGlobalmapManager();
    MarkerManagerClient getMarkerManager();

    MapImageUtils getMapImageUtils();

    // ------------------------------------------

    RadarPlayer getRadarPlayer();


    // ------------------------------------------

    // @TODO: remove, было для дебага
    boolean isAllowedToDisplay();
    void setAllowedToDisplay(Boolean draw, Boolean quietChange);

    // Key

    KeyBinding getKeyShowMinimap();
    KeyBinding getKeyShowGlobalmap();
    KeyBinding getKeyIncreaseZoom();
    KeyBinding getKeyDecreaseZoom();


}
