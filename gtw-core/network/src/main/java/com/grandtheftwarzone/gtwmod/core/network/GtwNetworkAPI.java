package com.grandtheftwarzone.gtwmod.core.network;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.GuiAction;
import com.grandtheftwarzone.gtwmod.api.map.data.CStartData;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.core.network.impl.*;
import com.grandtheftwarzone.gtwmod.core.network.impl.event.PacketEventConnect;
import com.grandtheftwarzone.gtwmod.core.network.impl.event.PacketHandlerEventConnect;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketFactoryGUI;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketGUIAction;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketHandlerFactoryGUI;
import com.grandtheftwarzone.gtwmod.core.network.impl.gui.PacketHandlerGUIAction;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.client.PacketMapClientHandlerRequest;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.client.PacketMapClientHandlerStartData;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.packet.PacketMapRequest;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.packet.PacketMapStartData;
import com.grandtheftwarzone.gtwmod.core.network.impl.map.server.PacketMapServerHandlerRequest;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
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
            System.out.println("PRIVET MIR");

            atumNetwork.registerMessage(PacketHandlerPlayerData.class, PacketPlayerData.class, Side.CLIENT);
            atumNetwork.registerMessage(PacketHandlerNotification.class, PacketNotification.class, Side.CLIENT);

            //GUI
            atumNetwork.registerMessage(PacketHandlerFactoryGUI.class, PacketFactoryGUI.class, Side.CLIENT);
            atumNetwork.registerMessage(PacketHandlerGUIAction.class, PacketGUIAction.class, Side.SERVER);

            // MAP
             atumNetwork.registerMessage(PacketMapClientHandlerRequest.class, PacketMapRequest.class, Side.CLIENT);
             atumNetwork.registerMessage(PacketMapServerHandlerRequest.class, PacketMapRequest.class, Side.SERVER);

             atumNetwork.registerMessage(PacketMapClientHandlerStartData.class, PacketMapStartData.class, Side.CLIENT);


             atumNetwork.registerMessage(PacketTestHandlerClinet.class, PacketTest.class, Side.CLIENT);
             atumNetwork.registerMessage(PacketTestHandlerServer.class, PacketTest.class, Side.SERVER);


             // EVENT
            atumNetwork.registerMessage(PacketHandlerEventConnect.class, PacketEventConnect.class, Side.CLIENT);

            System.out.println("POKA MIR");

        }
    }


    // Event

    public void sendConnect(EntityPlayerMP player) {
        atumNetwork.sendTo(new PacketEventConnect(), player);
    }

    //

    public void sendMapStartData(CStartData cStartData, EntityPlayerMP player) {
        atumNetwork.sendTo(new PacketMapStartData(cStartData), player);
    }

    public void sendTest(String str, EntityPlayerMP player) {
        System.out.println("Отправка клиенту test...");
        atumNetwork.sendTo(new PacketTest(
                str
        ), player);
    }

    public void sendTestServer(String str) {
        System.out.println("Отправка cерверу test...");
        atumNetwork.sendToServer(new PacketTest(
                str
        ));
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

    @Override
    public void sendSRequest(Config config) {
        atumNetwork.sendToServer(new PacketMapRequest(config));
    }


    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "networkManager";
    }
}
