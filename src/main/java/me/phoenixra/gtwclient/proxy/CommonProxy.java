package me.phoenixra.gtwclient.proxy;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.notification.NotificationRequest;
import me.phoenixra.gtwclient.networking.PacketHandlerNotification;
import me.phoenixra.gtwclient.networking.PacketHandlerPlayerData;
import me.phoenixra.gtwclient.networking.PacketNotification;
import me.phoenixra.gtwclient.networking.PacketPlayerData;
import me.phoenixra.gtwclient.networking.gui.PacketFactoryGUI;
import me.phoenixra.gtwclient.networking.gui.PacketGUIAction;
import me.phoenixra.gtwclient.networking.gui.PacketHandlerFactoryGUI;
import me.phoenixra.gtwclient.networking.gui.PacketHandlerGUIAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;
import java.util.function.Consumer;

public class CommonProxy {

    public static final SimpleNetworkWrapper NETWORK_CHANNEL =
            NetworkRegistry.INSTANCE.newSimpleChannel("gtwclient");
    private static int discriminator = 1;
    public CommonProxy(){
        MinecraftForge.EVENT_BUS.register(this);
        NETWORK_CHANNEL.registerMessage(PacketHandlerPlayerData.class,
                PacketPlayerData.class, discriminator++, Side.CLIENT);
        NETWORK_CHANNEL.registerMessage(PacketHandlerNotification.class,
                PacketNotification.class, discriminator++, Side.CLIENT);
        //GUI
        NETWORK_CHANNEL.registerMessage(PacketHandlerFactoryGUI.class,
                PacketFactoryGUI.class, discriminator++, Side.CLIENT);
        NETWORK_CHANNEL.registerMessage(PacketHandlerGUIAction.class,
                PacketGUIAction.class, discriminator++, Side.SERVER);

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    public void sendPlayerData(PlayerData pd, UUID playerUUID){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        NETWORK_CHANNEL.sendTo(new PacketPlayerData(pd), player);
    }
    public void sendNotification(NotificationRequest notification, UUID playerUUID){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        NETWORK_CHANNEL.sendTo(new PacketNotification(
                notification.getText(),
                notification.isPlaySound(),
                notification.getDisplayTime(),
                notification.getPositionX(),
                notification.getPositionY(),
                notification.getSizeX(),
                notification.getSizeY()
        ), player);
    }
    public void sendPacketFactoryGUI(UUID playerUUID,
                                     String factoryType,
                                     String factoryOwner,
                                     String level,
                                     String productionInfo,
                                     String storageInfo,
                                     String productionEfficiency,
                                     String storageEfficiency,
                                     String productionUpgradePrice,
                                     String storageUpgradePrice,
                                     double upgradeDelay,
                                     int actionType){
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        if(player == null) return;
        NETWORK_CHANNEL.sendTo(new PacketFactoryGUI(
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
    public void addGuiActionPacketConsumer(Consumer<String> consumer){
        PacketHandlerGUIAction.addPacketConsumer(consumer);
    }



}
