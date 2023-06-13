package me.phoenixra.playerhud;

import me.phoenixra.playerhud.proxy.CommonProxy;
import me.phoenixra.playerhud.sounds.SoundsHandler;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = PlayerHUD.MOD_ID,
        version = PlayerHUD.VERSION,
        name = PlayerHUD.NAME)
public class PlayerHUD {
    /** The mod ID of this mod */
    public static final String MOD_ID = "playerhud";
    /** The mod name of this mod */
    public static final String NAME = "Player-HUD";
    /** The mod version of this mod */
    public static final String VERSION = "1.0.0";

    /** The instance of this mod */
    @Mod.Instance
    public static PlayerHUD instance;

    @SidedProxy(clientSide = "me.phoenixra.playerhud.proxy.ClientProxy",
            serverSide = "me.phoenixra.playerhud.proxy.CommonProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        SoundsHandler.registerSounds();
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @SubscribeEvent(receiveCanceled = true)
    public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SoundsHandler.USER_LEVEL_UP);
    }

}
