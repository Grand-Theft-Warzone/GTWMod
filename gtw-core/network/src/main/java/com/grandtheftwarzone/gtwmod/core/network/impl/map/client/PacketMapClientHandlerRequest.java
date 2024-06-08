package com.grandtheftwarzone.gtwmod.core.network.impl.map.client;

import com.grandtheftwarzone.gtwmod.core.network.impl.map.packet.PacketMapRequest;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMapClientHandlerRequest implements IMessageHandler<PacketMapRequest, IMessage> {
    @Override
    public IMessage onMessage(PacketMapRequest message, MessageContext ctx) {
        return null;
    }
}
