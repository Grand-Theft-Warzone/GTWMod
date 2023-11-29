package com.grandtheftwarzone.gtwclient.core.minimap.listener;

import com.grandtheftwarzone.gtwclient.core.minimap.markers.MarkerManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerJoinListener {
    private final MarkerManager markerManager;

    public PlayerJoinListener(MarkerManager markerManager) {
        this.markerManager = markerManager;
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        event.player.sendMessage(new TextComponentString("Welcome to the server!"));
        markerManager.sendMarkers(event.player);
    }
}
