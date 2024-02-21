package com.grandtheftwarzone.gtwmod.core.network;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.GuiAction;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
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
import lombok.Getter;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class GtwNetworkAPI implements NetworkAPI {
    @Getter
    private NetworkManager atumNetwork = GtwAPI.getInstance().getGtwMod().getNetworkManager();

    public GtwNetworkAPI() {

        atumNetwork.registerMessage(PacketHandlerPlayerData.class, PacketPlayerData.class, Side.CLIENT);
        atumNetwork.registerMessage(PacketHandlerNotification.class, PacketNotification.class, Side.CLIENT);

        //GUI
        atumNetwork.registerMessage(PacketHandlerFactoryGUI.class, PacketFactoryGUI.class, Side.CLIENT);
        atumNetwork.registerMessage(PacketHandlerGUIAction.class, PacketGUIAction.class, Side.SERVER);
    }

    public void sendPlayerData(@NotNull PlayerData pd, @NotNull UUID playerUUID){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        atumNetwork.sendTo(new PacketPlayerData(pd), player);
    }
    public void sendNotification(@NotNull NotificationRequest notification, @NotNull UUID playerUUID){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        atumNetwork.sendTo(new PacketNotification(
                notification.getText(),
                notification.getDisplayTime()
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
        atumNetwork.sendTo(new PacketFactoryGUI(
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
        atumNetwork.sendToServer(new PacketGUIAction(playerUUID.toString(),guiId, action.getId()));
    }

    public void addGuiActionPacketConsumer(@NotNull Consumer<String> consumer){
        PacketHandlerGUIAction.addPacketConsumer(consumer);
    }

}
