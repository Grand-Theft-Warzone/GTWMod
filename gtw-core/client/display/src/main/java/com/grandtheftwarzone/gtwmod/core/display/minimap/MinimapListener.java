package com.grandtheftwarzone.gtwmod.core.display.minimap;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MinimapListener {

    //TODO: Remove in production
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) return;

        if (GtwMinimap.zoomInBinding.isKeyDown()) {
            GtwMinimap.getInstance().getMinimap().setZoom(
                    GtwMinimap.getInstance().getMinimap().getZoom() * 1.01f
            );
        } else if (GtwMinimap.zoomOutBinding.isKeyDown()) {
            GtwMinimap.getInstance().getMinimap().setZoom(
                    GtwMinimap.getInstance().getMinimap().getZoom() / 1.01f
            );
        }
    }
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if (Minecraft.getMinecraft().inGameHasFocus)
            GtwMinimap.getInstance().getMinimapRenderer().draw();
    }
}
