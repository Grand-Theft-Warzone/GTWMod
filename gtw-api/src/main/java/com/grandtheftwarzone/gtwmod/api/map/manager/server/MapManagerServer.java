package com.grandtheftwarzone.gtwmod.api.map.manager.server;

import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersServer;
import com.grandtheftwarzone.gtwmod.api.map.data.server.MapData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.PlayerMapData;
import com.grandtheftwarzone.gtwmod.api.map.data.RestrictionsData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public interface MapManagerServer {

    @Nullable MapData getMapData(String mapId);
    MapConsumersServer getMapConsumers();

    MarkerManagerServer getMarkerManager();

    void initConfig();
    String getDefaultMinimapId();
    String getDefaultGlobalmapId();

    RestrictionsData getRestrictionsData(UUID uuid);
    PlayerMapData getPlayerData(UUID uuid);
    @Nullable
    MapData getMapData(UUID uuid, String attachedTo, @Nullable String mapId);

}
