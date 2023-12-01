package com.grandtheftwarzone.gtwclient.core.minimap.listener;

import com.grandtheftwarzone.gtwclient.core.minimap.markers.ServerMarkerManager;
import lombok.AllArgsConstructor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@AllArgsConstructor
public class PlayerMarkerListener {
    private final ServerMarkerManager markerManager;

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        markerManager.sync(event.player.getUniqueID());
    }
}
