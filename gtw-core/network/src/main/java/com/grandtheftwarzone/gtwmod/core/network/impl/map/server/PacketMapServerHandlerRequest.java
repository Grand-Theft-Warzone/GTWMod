package com.grandtheftwarzone.gtwmod.core.network.impl.map.server;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.data.SRequest;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.packet.PacketMapRequest;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMapServerHandlerRequest implements IMessageHandler<PacketMapRequest, IMessage> {

    @Override
    public IMessage onMessage(PacketMapRequest message, MessageContext ctx) {
        SRequest sRequest = new SRequest(ctx.getServerHandler().player.getUniqueID(), message.getConfig());
        GtwAPI.getInstance().getMapManagerServer().getMapConsumers().getSRequest()
                .accept(sRequest);
        return null;

    }

}
