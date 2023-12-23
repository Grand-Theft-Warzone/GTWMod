package com.grandtheftwarzone.gtwmod.core.network;

import com.grandtheftwarzone.gtwmod.api.gui.GuiAction;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwmod.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.core.network.impl.PacketHandlerNotification;
import com.grandtheftwarzone.gtwmod.core.network.impl.PacketHandlerPlayerData;
import com.grandtheftwarzone.gtwmod.core.network.impl.PacketNotification;
import com.grandtheftwarzone.gtwmod.core.network.impl.PacketPlayerData;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketFactoryGUI;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketGUIAction;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketHandlerFactoryGUI;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketHandlerGUIAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class GtwNetworkManager implements NetworkManager {
    private final  SimpleNetworkWrapper NETWORK_CHANNEL;
    private int discriminator = 1;

    public GtwNetworkManager() {
        NETWORK_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("gtwclient");

        registerMessage(PacketHandlerPlayerData.class, PacketPlayerData.class, Side.CLIENT);
        registerMessage(PacketHandlerNotification.class, PacketNotification.class, Side.CLIENT);

        //GUI
        registerMessage(PacketHandlerFactoryGUI.class, PacketFactoryGUI.class, Side.CLIENT);
        registerMessage(PacketHandlerGUIAction.class, PacketGUIAction.class, Side.SERVER);
    }

    public void sendPlayerData(@NotNull PlayerData pd, @NotNull UUID playerUUID){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        sendTo(new PacketPlayerData(pd), player);
    }
    public void sendNotification(@NotNull NotificationRequest notification, @NotNull UUID playerUUID){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        sendTo(new PacketNotification(
                notification.getText(),
                notification.isPlaySound(),
                notification.getDisplayTime(),
                notification.getPositionX(),
                notification.getPositionY(),
                notification.getFontSize()
        ), player);
    }
    public void sendPacketFactoryGUI(@NotNull UUID playerUUID,
                                     @NotNull String factoryType,
                                     @NotNull String factoryOwner,
                                     @NotNull String level,
                                     @NotNull String productionInfo,
                                     @NotNull String storageInfo,
                                     @NotNull String productionEfficiency,
                                     @NotNull String storageEfficiency,
                                     @NotNull String productionUpgradePrice,
                                     @NotNull String storageUpgradePrice,
                                     double upgradeDelay,
                                     int actionType){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        sendTo(new PacketFactoryGUI(
                factoryType,
                factoryOwner,
                level,
                productionInfo,
                storageInfo,
                productionEfficiency,
                storageEfficiency,
                productionUpgradePrice,
                storageUpgradePrice,
                upgradeDelay,
                actionType
        ), player);
    }

    @Override
    public void sendPacketGuiAction(@NotNull UUID playerUUID, int guiId, GuiAction action) {
        sendToServer(new PacketGUIAction(playerUUID.toString(),guiId, action.getId()));
    }

    public void addGuiActionPacketConsumer(@NotNull Consumer<String> consumer){
        PacketHandlerGUIAction.addPacketConsumer(consumer);
    }

    @Override
    public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        NETWORK_CHANNEL.registerMessage(messageHandler, requestMessageType, discriminator++, side);
    }

    @Override
    public Packet<?> getPacketFrom(IMessage message) {
        return NETWORK_CHANNEL.getPacketFrom(message);
    }

    @Override
    public void sendToAll(IMessage message) {
        NETWORK_CHANNEL.sendToAll(message);
    }

    @Override
    public void sendTo(IMessage message, EntityPlayerMP player) {
        NETWORK_CHANNEL.sendTo(message, player);
    }

    @Override
    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllAround(message, point);
    }

    @Override
    public void sendToAllTracking(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllTracking(message, point);
    }

    @Override
    public void sendToAllTracking(IMessage message, Entity entity) {
        NETWORK_CHANNEL.sendToAllTracking(message, entity);
    }

    @Override
    public void sendToDimension(IMessage message, int dimensionId) {
        NETWORK_CHANNEL.sendToDimension(message, dimensionId);
    }

    @Override
    public void sendToServer(IMessage message) {
        NETWORK_CHANNEL.sendToServer(message);
    }

    @Override
    public SimpleNetworkWrapper getNetwork() {
        return NETWORK_CHANNEL;
    }
}
