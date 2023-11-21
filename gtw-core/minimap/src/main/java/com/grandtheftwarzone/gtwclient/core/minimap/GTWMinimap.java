package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.core.minimap.listener.MinimapListener;
import com.grandtheftwarzone.gtwclient.core.minimap.renderer.MinimapRenderer;
import com.grandtheftwarzone.gtwclient.core.minimap.utils.MapTexture;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Getter
@SideOnly(Side.CLIENT)
public class GTWMinimap {

    private final int radius = 45;
    private final int cornerDistance = 20;
    @Setter private float markerSize = 5f;
    @Setter private boolean rotating = true;

    private Minimap minimap;
    private MapTexture mapTexture;
    private MinimapRenderer minimapRenderer;

    @Getter private static GTWMinimap instance;

    public GTWMinimap() {
        instance = this;
    }

    public void initClient() {
        minimap = new Minimap();
        mapTexture = new MapTexture();
        minimapRenderer = new MinimapRenderer();
        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }
}
