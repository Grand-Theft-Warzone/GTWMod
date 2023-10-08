package com.grandtheftwarzone.gtwclient.core.network.impl;

import com.grandtheftwarzone.gtwclient.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.core.display.GtwPlayerHUD;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerNotification implements IMessageHandler<PacketNotification, IMessage> {
    @Override
    public IMessage onMessage(PacketNotification message, MessageContext ctx) {
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        pd.getNotificationQueue().add(new NotificationRequest(
                message.text,
                message.playSound,
                message.displayTime,
                message.positionX,
                message.positionY,
                message.fontSize
                )
        );
        return null;
    }
}
