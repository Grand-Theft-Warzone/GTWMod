package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersServer;
import com.grandtheftwarzone.gtwmod.api.map.data.server.MapData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.PlayerMapData;
import com.grandtheftwarzone.gtwmod.api.map.data.RestrictionsData;

import javax.annotation.Nullable;
import java.util.UUID;

public interface MapManagerServer {

    MapConsumersServer getMapConsumers();

    void initConfig();
    String getDefaultMinimapId();
    String getDefaultGlobalmapId();

    RestrictionsData getRestrictionsData(UUID uuid);
    PlayerMapData getPlayerData(UUID uuid);
    @Nullable
    MapData getMapData(UUID uuid, String attachedTo, @Nullable String mapId);

}
