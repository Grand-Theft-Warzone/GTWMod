package com.grandtheftwarzone.gtwmod.core.network.impl;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTestHandlerServer implements IMessageHandler<PacketTest, IMessage>  {
    @Override
    public IMessage onMessage(PacketTest message, MessageContext ctx) {
        return null;
    }
}
