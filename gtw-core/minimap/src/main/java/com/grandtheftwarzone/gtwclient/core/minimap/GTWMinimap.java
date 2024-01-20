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

import java.util.Arrays;

@Getter
public class GTWMinimap {

    private final int radius = 48;
    private final int cornerDistance = 20;
    @Setter private boolean rotating = true;

    private final float scale = 1f;
    private final int textureSize = 128;

    private Minimap minimap;
    private MapTexture mapTexture;
    private MinimapRenderer minimapRenderer;

    private ChunkManager chunkManager;

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
      /*  serverMarkerManager.insert(Arrays.asList(
                new Marker(185, -189, MarkerType.HOME),
                new Marker(170, -190, MarkerType.CLOTHES_SHOP),
                new Marker(180, -170, MarkerType.WEAPONS_SHOP)
        ));*/

        MinecraftForge.EVENT_BUS.register(new PlayerMarkerListener(serverMarkerManager));
    }

    private void initClient() {
        minimap = new Minimap();
        mapTexture = new MapTexture(textureSize);
        minimapRenderer = new MinimapRenderer();
        chunkManager = new ChunkManager();
        clientMarkerManager = new ClientMarkerManager();

        //To test only client
        clientMarkerManager.syncMarkers(Arrays.asList(
                new Marker(185, -189, MarkerType.HOME),
                new Marker(170, -190, MarkerType.CLOTHES_SHOP),
                new Marker(180, -170, MarkerType.WEAPONS_SHOP)
        ));

        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }
}
