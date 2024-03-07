package com.grandtheftwarzone.gtwmod.core.network.impl.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.data.SRequest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.eventhandler.Event;
public class PacketServerHandlerRequestMap implements IMessageHandler<PacketRequestMap, IMessage> {

    @Override
    public IMessage onMessage(PacketRequestMap message, MessageContext ctx) {
        System.out.println("ТУТ 1 ОБРАБОТКА onMassage");
        SRequest sRequest = new SRequest(ctx.getServerHandler().player.getUniqueID(), message.getConfig());
        System.out.println("ТУТ 2 ОБРАБОТКА onMassage");
        GtwAPI.getInstance().getMapManagerServer().getMapConsumers().getSRequest()
                .accept(sRequest);
        return null;

    }

}
