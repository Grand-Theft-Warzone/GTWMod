package com.grandtheftwarzone.gtwmod.core.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.minimap.Marker;
import com.grandtheftwarzone.gtwmod.api.minimap.MarkerType;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import lombok.Getter;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class GTWMinimapServer implements AtumModService {
    private NetworkAPI networkManager;
    @Getter
    private ServerMarkerManager markerManager;

    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if (fmlEvent instanceof net.minecraftforge.fml.common.event.FMLInitializationEvent) {
            init();
        }
    }

    private void init() {
        this.networkManager = GtwAPI.getInstance().getNetworkManager();
        this.markerManager = new ServerMarkerManager(networkManager);

        //TODO: Remove this in production
        Collection<Marker> markers = new ArrayList<>();
        markers.add(new Marker(10, 10, MarkerType.PLAYER));
        markers.add(new Marker(20, 20, MarkerType.CLOTHES_SHOP));
        markerManager.insert(markers);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        markerManager.sync(event.player.getUniqueID());
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "minimap_server";
    }
}
