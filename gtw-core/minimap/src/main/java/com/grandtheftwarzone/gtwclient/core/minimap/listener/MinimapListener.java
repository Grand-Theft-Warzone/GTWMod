package com.grandtheftwarzone.gtwclient.core.minimap.listener;

import com.grandtheftwarzone.gtwclient.core.minimap.renderer.MinimapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MinimapListener {
    MinimapRenderer minimapRenderer;

    public MinimapListener() {
        minimapRenderer = new MinimapRenderer();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) return;

        minimapRenderer.updateMapData(Minecraft.getMinecraft().world);
        minimapRenderer.updateMapTexture();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if (Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            int screenWidth = resolution.getScaledWidth();
            int screenHeight = resolution.getScaledHeight();
            int hudSpacing = 5;
            int miniMapSize = 45;


            minimapRenderer.renderMiniMap(hudSpacing + miniMapSize, screenHeight - miniMapSize - hudSpacing, miniMapSize);
        }
    }
}
