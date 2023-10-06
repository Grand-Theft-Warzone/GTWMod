package me.phoenixra.gtwclient.networking;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.notification.NotificationRequest;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerNotification implements IMessageHandler<PacketNotification, IMessage> {
    @Override
    public IMessage onMessage(PacketNotification message, MessageContext ctx) {
        PlayerData pd = Hud.instance.playerData;
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
