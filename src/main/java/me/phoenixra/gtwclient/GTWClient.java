package me.phoenixra.gtwclient;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.config.category.ConfigCategory;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.gtwclient.fml.loadingscreen.MainSplashRenderer;
import me.phoenixra.gtwclient.proxy.CommonProxy;
import me.phoenixra.gtwclient.screen.ModLoadingListener;
import me.phoenixra.gtwclient.sounds.SoundsHandler;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = "gtwclient",
        version = "1.0.0",
        name = "GTWClient")
public class GTWClient extends AtumMod {

    public static final boolean IS_DEV_ENVIRONMENT = false;

    @Getter
    private Config settings;

    @Getter
    public File resourcesFolder;

    /** The instance of this mod */
    @Mod.Instance
    public static GTWClient instance;

    @SidedProxy(clientSide = "me.phoenixra.gtwclient.proxy.ClientProxy",
            serverSide = "me.phoenixra.gtwclient.proxy.CommonProxy")
    public static CommonProxy proxy;


    public GTWClient(){
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            settings = getApi().createLoadableConfig(this,
                    "settings",
                    "",
                    ConfigType.JSON,
                    false
            );
            getConfigManager().addConfigCategory(new ConfigCategory(
                    this,
                    ConfigType.JSON,
                    "display",
                    "display",
                    true) {
                private List<String> elements = new ArrayList<>();

                @Override
                protected void clear() {
                    elements.forEach(it->getDisplayElementRegistry().unregister(it));
                }

                @Override
                protected void acceptConfig(@NotNull String id, @NotNull Config config) {
                    getAtumMod().getLogger().info("Loading display element with id " + id);
                    if(getDisplayElementRegistry().getElementById(id) != null) {
                        getAtumMod().getLogger().warn("Display element with id " + id + " already added or is a default element!");
                        return;
                    }

                    DisplayElement element = getDisplayElementRegistry().compile(config);
                    if (element != null) {
                        getDisplayElementRegistry().register(id, element);
                        elements.add(id);
                    }
                }
            });
        }
    }
    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            getConfigManager().reloadAllConfigCategories();
        }
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        resourcesFolder = event.getModConfigurationDirectory();
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
        event.getRegistry().registerAll(SoundsHandler.getAllSounds());
    }

    @Mod.EventHandler
    public static void construct(FMLConstructionEvent event) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            ModLoadingListener.setup();
            MainSplashRenderer.onReachConstruct();
        }
    }

    @Override
    public @NotNull String getName() {
        return "GTWClient";
    }

    @Override
    public @NotNull String getModID() {
        return "gtwclient";
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }
}
