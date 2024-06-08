package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.data.CStartData;
import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import com.grandtheftwarzone.gtwmod.api.map.data.RestrictionsData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.MapData;

import java.util.UUID;


public class ProcessConsumer {

    public ProcessConsumer() {
        request();
    }


    public void request() {
        GtwAPI.getInstance().getMapManagerServer().getMapConsumers().setSRequest(
                (it) ->{
                    GtwLog.getLogger().debug("WE HAVE RECEIVED A REQUEST FROM A CLIENT:");
                    GtwLog.getLogger().debug("UUID: " + it.getUuid());
                    GtwLog.getLogger().debug("Event: " + it.getConfig().getString("event"));

                    String event = it.getConfig().getString("event");
                    switch (event) {
                        case "getStartData":
                            getStartData(it.getUuid());
                    }
                }
        );
    }

    public static void getStartData(UUID uuid) {

        MapImageData minimap = null, globalmap = null;

        MapData minimapData = GtwAPI.getInstance().getMapManagerServer().getMapData(uuid, "mini", GtwAPI.getInstance().getMapManagerServer().getPlayerData(uuid).getMinimapId());
        if (minimapData != null) {
            minimap = minimapData.toMapImageData();
        }
        MapData globalmapData = GtwAPI.getInstance().getMapManagerServer().getMapData(uuid, "global", GtwAPI.getInstance().getMapManagerServer().getPlayerData(uuid).getGlobalmapId());
        if (globalmapData != null) {
            globalmap = globalmapData.toMapImageData();
        }
        RestrictionsData restrictionsData = GtwAPI.getInstance().getMapManagerServer().getRestrictionsData(uuid);
        GtwAPI.getInstance().getNetworkAPI().sendMapStartData(new CStartData(minimap, globalmap, restrictionsData), GtwAPI.getInstance().getServer().getPlayerList().getPlayerByUUID(uuid));
    }


}
