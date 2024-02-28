package com.grandtheftwarzone.gtwmod.core.network.impl.minimap;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClientHandlerRequestMap implements IMessageHandler<PacketRequestMap, IMessage> {
    @Override
    public IMessage onMessage(PacketRequestMap message, MessageContext ctx) {
        return null;
    }
}
