package com.grandtheftwarzone.gtwclient.core.minimap.listener;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MinimapListener {
    /*
    @SubscribeEvent
    public void onChunkUpdate(ChunkEvent event) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) return;

        if (event instanceof ChunkEvent.Load) {
            GTWMinimap.getInstance().getChunkManager().addChunk(event.getChunk());
        } else if (event instanceof ChunkEvent.Unload) {
            GTWMinimap.getInstance().getChunkManager().removeChunk(event.getChunk());
        }
    }*/


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) return;


        if (GTWMinimap.zoomInBinding.isKeyDown()) {
            GTWMinimap.getInstance().getMinimap().setZoom(
                    GTWMinimap.getInstance().getMinimap().getZoom() * 1.01f
            );
        } else if (GTWMinimap.zoomOutBinding.isKeyDown()) {
            GTWMinimap.getInstance().getMinimap().setZoom(
                    GTWMinimap.getInstance().getMinimap().getZoom() / 1.01f
            );
        }
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if (Minecraft.getMinecraft().inGameHasFocus)
            GTWMinimap.getInstance().getMinimapRenderer().draw();
    }
}
