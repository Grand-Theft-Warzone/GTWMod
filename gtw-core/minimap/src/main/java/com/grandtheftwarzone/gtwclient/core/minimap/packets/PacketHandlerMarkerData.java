package com.grandtheftwarzone.gtwclient.core.minimap.packets;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.MarkerManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerMarkerData implements IMessageHandler<PacketMarkerData, IMessage> {
    @Override
    public IMessage onMessage(PacketMarkerData message, MessageContext ctx) {
        if (!ctx.side.isClient()) return null;

        Minecraft.getMinecraft().player.sendChatMessage("Received markers");

        MarkerManager markerManager = GTWMinimap.getInstance().getMarkerManager();
        markerManager.updateMarkers(message.getMarkers());

        return null;
    }
}
