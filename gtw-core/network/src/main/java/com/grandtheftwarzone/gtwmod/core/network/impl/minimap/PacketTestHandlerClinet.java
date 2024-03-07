package com.grandtheftwarzone.gtwmod.core.network.impl.minimap;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTestHandlerClinet implements IMessageHandler<PacketTest, IMessage>  {
    @Override
    public IMessage onMessage(PacketTest message, MessageContext ctx) {
        System.out.println("BlaBlaBlaBla");
        return null;
    }
}
