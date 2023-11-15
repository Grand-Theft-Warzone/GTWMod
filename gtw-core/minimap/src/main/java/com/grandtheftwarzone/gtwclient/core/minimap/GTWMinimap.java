package com.grandtheftwarzone.gtwclient.core.minimap;

import com.grandtheftwarzone.gtwclient.core.minimap.listener.MinimapListener;
import com.grandtheftwarzone.gtwclient.core.minimap.renderer.MinimapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GTWMinimap {

    public GTWMinimap() {
        MinecraftForge.EVENT_BUS.register(new MinimapListener());
    }

}
