package com.grandtheftwarzone.gtwmod.core.network.impl;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerNotification implements IMessageHandler<PacketNotification, IMessage> {
    @Override
    public IMessage onMessage(PacketNotification message, MessageContext ctx) {
        PlayerData pd = GtwAPI.getInstance().getPlayerData();
        GtwAPI.getInstance().getGtwMod().getDisplayManager()
                        .getHUDCanvas().getDisplayRenderer()
                        .getDisplayData()
                .setTemporaryData(
                        "notification",
                        message.text,
                        message.displayTime,
                        true
                );
        return null;
    }
}
