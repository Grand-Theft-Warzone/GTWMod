package com.grandtheftwarzone.gtwclient.core.minimap.packets;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.ClientMarkerManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerMarkerData implements IMessageHandler<PacketMarkerData, IMessage> {
    @Override
    public IMessage onMessage(PacketMarkerData message, MessageContext ctx) {
        if (!ctx.side.isClient()) return null;

        ClientMarkerManager markerManager = GTWMinimap.getInstance().getClientMarkerManager();
        markerManager.syncMarkers(message.getMarkers());

        return null;
    }
}
