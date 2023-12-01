package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.core.minimap.listener.MinimapListener;
import com.grandtheftwarzone.gtwclient.core.minimap.listener.PlayerMarkerListener;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.ClientMarkerManager;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.MarkerType;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.ServerMarkerManager;
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

    private ServerMarkerManager serverMarkerManager;
    private ClientMarkerManager clientMarkerManager;

    @Getter private static GTWMinimap instance;

    public GTWMinimap() {
        instance = this;
    }

    public void init(NetworkManager networkManager) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) initClient();
        else initServer(networkManager);

        networkManager.registerMessage(PacketHandlerMarkerData.class, PacketMarkerData.class, Side.CLIENT);
    }

    private void initServer(NetworkManager networkManager) {
        serverMarkerManager = new ServerMarkerManager(networkManager);
        serverMarkerManager.insert(new Marker(0, 0, MarkerType.HOME));
        serverMarkerManager.insert(new Marker(10, 10, MarkerType.GARAGE));
        serverMarkerManager.insert(new Marker(25, 25, MarkerType.FACTORY));

        MinecraftForge.EVENT_BUS.register(new PlayerMarkerListener(serverMarkerManager));
    }

    private void initClient() {
        minimap = new Minimap();
        mapTexture = new MapTexture();
        minimapRenderer = new MinimapRenderer();
        clientMarkerManager = new ClientMarkerManager();

        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }
}
