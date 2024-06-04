package com.grandtheftwarzone.gtwmod.api.networking;

import com.grandtheftwarzone.gtwmod.api.gui.GuiAction;
import com.grandtheftwarzone.gtwmod.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public interface NetworkAPI {

    @NotNull
    NetworkManager getAtumNetwork();

    /**
     * Send player data to the client.
     *
     * @param pd The player data to send
     * @param playerUUID The player to send it to
     */
    @SideOnly(Side.SERVER)
    void sendPlayerData(@NotNull PlayerData pd,
                        @NotNull UUID playerUUID);

    /**
     * Send a notification to the client.
     *
     * @param notification The notification to send
     * @param playerUUID The player to send it to
     */
    @SideOnly(Side.SERVER)
    void sendNotification(@NotNull NotificationRequest notification,
                          @NotNull UUID playerUUID);

    /**
     * Send a packet to the server to request a factory gui action.
     *
     * @param playerUUID The player requesting the action
     * @param factoryType The type of factory
     * @param factoryOwner The owner of the factory
     * @param level The level of the factory
     * @param productionInfo The production info of the factory
     * @param storageInfo The storage info of the factory
     * @param productionEfficiency The production efficiency of the factory
     * @param storageEfficiency The storage efficiency of the factory
     * @param productionUpgradePrice The production upgrade price of the factory
     * @param storageUpgradePrice The storage upgrade price of the factory
     * @param upgradeDelay The upgrade delay of the factory
     * @param actionType The type of action
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


}
