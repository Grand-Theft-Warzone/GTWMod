package com.grandtheftwarzone.gtwmod.core.network.impl.map.client;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.packet.PacketMapMarkers;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketMapClientHandlerMarkers implements IMessageHandler<PacketMapMarkers, IMessage> {
    @Override
    public IMessage onMessage(PacketMapMarkers message, MessageContext ctx) {
//        System.out.println("Вызывается onMessage " + getClass().getSimpleName());
        List<TemplateMarker> markers = message.getMarkers();
        if (markers == null) {
            System.out.print("AQA");
        }
        GtwAPI.getInstance().getMapManagerClient().getMapConsumers().getCMarkersList().accept(markers);
        return null;
    }
}
