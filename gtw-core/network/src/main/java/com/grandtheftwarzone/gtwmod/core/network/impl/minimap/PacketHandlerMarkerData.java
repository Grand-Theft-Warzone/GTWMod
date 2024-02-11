package com.grandtheftwarzone.gtwmod.core.network.impl.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerMarkerData implements IMessageHandler<PacketMarkerData, IMessage> {
    @Override
    public IMessage onMessage(PacketMarkerData message, MessageContext ctx) {
        if (!ctx.side.isClient()) return null;

        GtwAPI.getInstance().getClientMarkerManager().syncMarkers(message.getMarkers());
        return null;
    }
}
