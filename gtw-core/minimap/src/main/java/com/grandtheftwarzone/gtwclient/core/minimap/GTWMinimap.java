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
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Shader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

@Getter
public class GTWMinimap {

    private final int radius = 48;
    private final int cornerDistance = 20;
    @Setter private boolean rotating = true;

    private TexturedMinimap minimap;
    private MinimapRenderer minimapRenderer;

    private ServerMarkerManager serverMarkerManager;
    private ClientMarkerManager clientMarkerManager;

    @Getter private static GTWMinimap instance;

    public static KeyBinding zoomInBinding;
    public static KeyBinding zoomOutBinding;
    public static KeyBinding goToMapCenterBinding;


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
        minimap = new TexturedMinimap(2593, 2281, -512, -2080, 2080, 200,  1f);
        minimapRenderer = new MinimapRenderer();
        clientMarkerManager = new ClientMarkerManager();

        zoomInBinding = new KeyBinding("Zoom In", Keyboard.KEY_Z, "Minimap");
        zoomOutBinding = new KeyBinding("Zoom out", Keyboard.KEY_X, "Minimao");

        ClientRegistry.registerKeyBinding(zoomInBinding);
        ClientRegistry.registerKeyBinding(zoomOutBinding);
        ClientRegistry.registerKeyBinding(goToMapCenterBinding);


        //To test only client
        clientMarkerManager.syncMarkers(Arrays.asList(
                new Marker(15, 19, MarkerType.HOME),
                new Marker(30, -19, MarkerType.CLOTHES_SHOP),
                new Marker(50, 7, MarkerType.WEAPONS_SHOP)
        ));

        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }
}
