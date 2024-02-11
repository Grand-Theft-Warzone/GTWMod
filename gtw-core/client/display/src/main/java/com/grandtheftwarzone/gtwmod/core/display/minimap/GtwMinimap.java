package com.grandtheftwarzone.gtwmod.core.display.minimap;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

@Getter
public class GtwMinimap {

    private final int radius = 48;
    private final int cornerDistance = 20;
    @Setter private boolean rotating = true;

    private final long mapStartX = -512;
    private final long mapStartY = -2080;
    private final long mapEndX = 2080;
    private final long mapEndY = 200;

    private final int mapTextureWidth = 2593;
    private final int mapTextureHeight = 2281;

    private final float startZoom = 0.5f;

    private TexturedMinimap minimap;
    private MinimapRenderer minimapRenderer;

    private ClientMarkerManager clientMarkerManager;

    @Getter private static GtwMinimap instance;

    //TODO: Remove in production
    public static KeyBinding zoomInBinding;
    public static KeyBinding zoomOutBinding;


    public GtwMinimap() {
        instance = this;
    }


    public void init() {
        minimap = new TexturedMinimap(mapTextureWidth, mapTextureHeight, mapStartX, mapStartY, mapEndX, mapEndY, startZoom);
        minimapRenderer = new MinimapRenderer();
        clientMarkerManager = new ClientMarkerManager();

        //TODO: Remove in production
        zoomInBinding = new KeyBinding("Zoom In", Keyboard.KEY_Z, "Minimap");
        zoomOutBinding = new KeyBinding("Zoom out", Keyboard.KEY_X, "Minimap");

        ClientRegistry.registerKeyBinding(zoomInBinding);
        ClientRegistry.registerKeyBinding(zoomOutBinding);


        //To test only client
       /* clientMarkerManager.syncMarkers(Arrays.asList(
                new Marker(15, 19, MarkerType.HOME),
                new Marker(30, -19, MarkerType.CLOTHES_SHOP),
                new Marker(50, 7, MarkerType.WEAPONS_SHOP)
        ));*/

        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }
}
