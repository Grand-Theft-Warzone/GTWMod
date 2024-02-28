package com.grandtheftwarzone.gtwmod.server;

import com.grandtheftwarzone.gtwmod.core.killFeed.GTWKillFeed;
import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.GtwProperties;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.core.map.GtwServerMapManager;
import com.grandtheftwarzone.gtwmod.core.misc.GtwSoundsManager;
import com.grandtheftwarzone.gtwmod.core.network.GtwNetworkAPI;
import com.grandtheftwarzone.gtwmod.server.proxy.CommonProxy;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@Mod(modid = GtwProperties.MOD_ID,
        version = GtwProperties.VERSION,
        name = GtwProperties.MOD_NAME)
public class GTWModServer extends AtumMod {
    @Mod.Instance
    public static GTWModServer instance;

    @SidedProxy(serverSide = "com.grandtheftwarzone.gtwmod.server.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Getter @Setter
    private NetworkAPI networkAPI;
    @Getter
    private GtwSoundsManager soundsManager;

    @Getter
    private GTWKillFeed killFeed;

    @Getter
    private GtwServerMapManager map;

    @Getter
    private MinecraftServer server;


    public GTWModServer(){
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            throw new RuntimeException("This mod is server side only!");
        }

        GtwLog.setLogger(getLogger());

        GtwLog.getLogger().info(GtwAPI.getGtwAsciiArt());
        GtwLog.getLogger().info("Initializing GTWMod[server]...");
        instance = this;
        GtwAPI.Instance.set(new GtwAPIServer());

        //services
        networkAPI = new GtwNetworkAPI(this);
        soundsManager = new GtwSoundsManager();
        killFeed = new GTWKillFeed(this);
        map = new GtwServerMapManager(this);

    }





    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        provideModService(proxy);

        notifyModServices(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        this.server = event.getServer();
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        notifyModServices(event);
    }

    @Override
    public @NotNull String getName() {
        return GtwProperties.MOD_NAME;
    }

    @Override
    public @NotNull String getModID() {
        return GtwProperties.MOD_ID;
    }

    @Override
    public @NotNull String getPackagePath() {
        return "com.grandtheftwarzone.gtwmod";
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }
}
