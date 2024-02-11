package com.grandtheftwarzone.gtwmod.api.networking;

import com.grandtheftwarzone.gtwmod.api.gui.GuiAction;
import com.grandtheftwarzone.gtwmod.api.minimap.Marker;
import com.grandtheftwarzone.gtwmod.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public interface NetworkAPI {

    /**
     * Send player data to the client.
     *
     * @param pd         The player data to send
     * @param playerUUID The player to send it to
     */
    @SideOnly(Side.SERVER)
    void sendPlayerData(@NotNull PlayerData pd,
                        @NotNull UUID playerUUID);

    /**
     * Send a notification to the client.
     *
     * @param notification The notification to send
     * @param playerUUID   The player to send it to
     */
    @SideOnly(Side.SERVER)
    void sendNotification(@NotNull NotificationRequest notification,
                          @NotNull UUID playerUUID);

    /**
     * Send a packet to the server to request a factory gui action.
     *
     * @param playerUUID             The player requesting the action
     * @param factoryType            The type of factory
     * @param factoryOwner           The owner of the factory
     * @param level                  The level of the factory
     * @param productionInfo         The production info of the factory
     * @param storageInfo            The storage info of the factory
     * @param productionEfficiency   The production efficiency of the factory
     * @param storageEfficiency      The storage efficiency of the factory
     * @param productionUpgradePrice The production upgrade price of the factory
     * @param storageUpgradePrice    The storage upgrade price of the factory
     * @param upgradeDelay           The upgrade delay of the factory
     * @param actionType             The type of action
     */
    @SideOnly(Side.SERVER)
    void sendPacketFactoryGUI(@NotNull UUID playerUUID,
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
                              int actionType);


    @SideOnly(Side.CLIENT)
    void sendPacketGuiAction(@NotNull UUID playerUUID, int guiId, GuiAction action);

    /**
     * Register a consumer for a gui action packet.
     *
     * @param consumer The consumer to register
     */
    @SideOnly(Side.SERVER)
    void addGuiActionPacketConsumer(Consumer<String> consumer);

    @SideOnly(Side.SERVER)
    void sendMarkerData(@NotNull UUID playerUUID, @NotNull ArrayList<Marker> markers);

    @SideOnly(Side.SERVER)
    void sendMarkerDataToAll(@NotNull ArrayList<Marker> markers);


    /**
     * Register a message handler for a message type.
     */
    <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> messageHandler,
            Class<REQ> requestMessageType,
            Side side
    );


    /**
     * Construct a minecraft packet from the supplied message.
     *
     * @param message The message to translate into packet form
     * @return A minecraft {@link Packet} suitable for use in minecraft APIs
     */
    Packet<?> getPacketFrom(IMessage message);

    /**
     * Send this message to everyone.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     *
     * @param message The message to send
     */
    void sendToAll(IMessage message);

    /**
     * Send this message to the specified player.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    void sendTo(IMessage message, EntityPlayerMP player);

    /**
     * Send this message to everyone within a certain range of a point.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     *
     * @param message The message to send
     * @param point   The {@link NetworkRegistry.TargetPoint} around which to send
     */
    void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point);

    /**
     * Sends this message to everyone tracking a point.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     * The {@code range} field of the {@link NetworkRegistry.TargetPoint} is ignored.
     *
     * @param message The message to send
     * @param point   The tracked {@link NetworkRegistry.TargetPoint} around which to send
     */
    void sendToAllTracking(IMessage message, NetworkRegistry.TargetPoint point);

    /**
     * Sends this message to everyone tracking an entity.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     * This is not equivalent to {@link #sendToAllTracking(IMessage, NetworkRegistry.TargetPoint)}
     * because entities have different tracking distances based on their type.
     *
     * @param message The message to send
     * @param entity  The tracked entity around which to send
     */
    void sendToAllTracking(IMessage message, Entity entity);

    /**
     * Send this message to everyone within the supplied dimension.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    void sendToDimension(IMessage message, int dimensionId);

    /**
     * Send this message to the server.
     * The {@link IMessageHandler} for this message type should be on the SERVER side.
     *
     * @param message The message to send
     */
    void sendToServer(IMessage message);


    /**
     * Get network handle
     */
    SimpleNetworkWrapper getNetwork();
}
