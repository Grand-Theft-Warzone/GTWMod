package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.core.minimap.listener.MinimapListener;
import com.grandtheftwarzone.gtwclient.core.minimap.listener.PlayerJoinListener;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.MarkerManager;
import com.grandtheftwarzone.gtwclient.core.minimap.packets.PacketHandlerMarkerData;
import com.grandtheftwarzone.gtwclient.core.minimap.packets.PacketMarkerData;
import com.grandtheftwarzone.gtwclient.core.minimap.renderer.MinimapRenderer;
import com.grandtheftwarzone.gtwclient.core.minimap.utils.MapTexture;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

@Getter
public class GTWMinimap {

    private final int radius = 45;
    private final int cornerDistance = 20;
    @Setter private float markerSize = 2f;
    @Setter private boolean rotating = true;

    private Minimap minimap;
    private MapTexture mapTexture;
    private MinimapRenderer minimapRenderer;

    private MarkerManager markerManager;

    @Getter private static GTWMinimap instance;

    public GTWMinimap() {
        instance = this;

        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
            MinecraftForge.EVENT_BUS.register(new PlayerJoinListener(markerManager));
    }

    public void init(NetworkManager networkManager) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) initClient();
        else initServer(networkManager);

        //TODO: Sync markers with clients
        networkManager.registerMessage(PacketHandlerMarkerData.class, PacketMarkerData.class, Side.CLIENT);
    }

    private void initServer(NetworkManager networkManager) {
        markerManager = new MarkerManager(networkManager);
        markerManager.insertMarker(new Marker(10, 10, Marker.MarkerType.HOUSE));
        markerManager.insertMarker(new Marker(25, 25, Marker.MarkerType.HOSPITAL));
        markerManager.insertMarker(new Marker(-5, 10, Marker.MarkerType.PLAYER));
//        markerManager.syncMarkers();

    }

    private void initClient() {
        minimap = new Minimap();
        mapTexture = new MapTexture();
        minimapRenderer = new MinimapRenderer();
        markerManager = new MarkerManager(null);

        markerManager.insertMarker(new Marker(10, 10, Marker.MarkerType.HOUSE));
        markerManager.insertMarker(new Marker(25, 25, Marker.MarkerType.HOSPITAL));
        markerManager.insertMarker(new Marker(-5, 10, Marker.MarkerType.PLAYER));
        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }
}
