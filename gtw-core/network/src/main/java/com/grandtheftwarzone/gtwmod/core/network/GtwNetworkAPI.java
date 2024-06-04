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
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class GtwNetworkAPI implements NetworkAPI, AtumModService {
    @Getter
    private NetworkManager atumNetwork = GtwAPI.getInstance().getGtwMod().getNetworkManager();

    public GtwNetworkAPI(AtumMod atumMod) {
        atumMod.provideModService(this);
    }
    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLInitializationEvent){
            atumNetwork.registerMessage(PacketHandlerPlayerData.class, PacketPlayerData.class, Side.CLIENT);
            atumNetwork.registerMessage(PacketHandlerNotification.class, PacketNotification.class, Side.CLIENT);

            //GUI
            atumNetwork.registerMessage(PacketHandlerFactoryGUI.class, PacketFactoryGUI.class, Side.CLIENT);
        }
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
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "networkManager";
    }
}
