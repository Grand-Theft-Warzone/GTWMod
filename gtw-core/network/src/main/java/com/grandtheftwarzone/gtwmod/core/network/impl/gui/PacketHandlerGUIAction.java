package com.grandtheftwarzone.gtwmod.core.network.impl.gui;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PacketHandlerGUIAction implements IMessageHandler<PacketGUIAction, IMessage> {

    private static List<Consumer<String>> packetConsumers = new ArrayList<>();

    @Override
    public IMessage onMessage(PacketGUIAction message, MessageContext ctx) {
        List<Consumer<String>> list = new ArrayList<>(packetConsumers);
        packetConsumers.clear();
        list.forEach((it)->it.accept(message.getPlayerUUID()+";"+message.getGuiID()+";"+message.getAction()));
        System.out.println("Privet");
        return null;
    }

    public static void addPacketConsumer(Consumer<String> consumer){
        packetConsumers.add(consumer);
    }
}
