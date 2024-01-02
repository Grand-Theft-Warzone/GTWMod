package com.grandtheftwarzone.gtwmod.server;

import com.grandtheftwarzone.gtwmod.core.killFeed.GTWKillFeed;
import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.GtwProperties;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.server.proxy.CommonProxy;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
    private GTWKillFeed killFeed;


    public GTWModServer(){
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            throw new RuntimeException("This mod is server side only!");
        }
        GtwLog.info(GtwAPI.getGtwAsciiArt());
        GtwLog.info("Initializing GTWMod[server]...");
        instance = this;
        GtwAPI.Instance.set(new GtwAPIServer());

        killFeed = new GTWKillFeed();

    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
//        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
//        killFeed.syncConfig(config);
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        killFeed.initConfig();
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
    public boolean isDebugEnabled() {
        return false;
    }
}
