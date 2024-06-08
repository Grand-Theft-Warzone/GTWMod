package com.grandtheftwarzone.gtwmod.core.network.impl.event;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.event.ClientConnectEvent;
import com.grandtheftwarzone.gtwmod.api.event.GtwEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerEventConnect implements IMessageHandler<PacketEventConnect, IMessage> {
    @Override
    public IMessage onMessage(PacketEventConnect message, MessageContext ctx) {
        MinecraftForge.EVENT_BUS.post(new ClientConnectEvent());
        return null;
    }
}
