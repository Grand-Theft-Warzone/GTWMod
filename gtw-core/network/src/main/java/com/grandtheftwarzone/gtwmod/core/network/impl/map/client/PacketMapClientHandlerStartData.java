package com.grandtheftwarzone.gtwmod.core.network.impl.map.client;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.data.CStartData;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.packet.PacketMapStartData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.function.Consumer;

public class PacketMapClientHandlerStartData implements IMessageHandler<PacketMapStartData, IMessage> {


    @Override
    public IMessage onMessage(PacketMapStartData message, MessageContext ctx) {
        CStartData cStartData = new CStartData(message.getMinimapData(), message.getGlobalmapData(), message.getRestrictionsData());
        GtwAPI.getInstance().getMapManagerClient().getMapConsumers().getCStartData().accept(cStartData);
        return null;
    }
}
