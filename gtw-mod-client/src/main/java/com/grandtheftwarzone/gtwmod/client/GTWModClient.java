package com.grandtheftwarzone.gtwmod.client;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwProperties;
import com.grandtheftwarzone.gtwmod.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneGui;
import com.grandtheftwarzone.gtwmod.api.hud.PlayerHUD;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import com.grandtheftwarzone.gtwmod.client.proxy.ClientProxy;
import com.grandtheftwarzone.gtwmod.core.display.GtwFactoryGuiHandler;
import com.grandtheftwarzone.gtwmod.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwmod.core.display.GtwScreensManager;
import com.grandtheftwarzone.gtwmod.core.display.loadingscreen.MainSplashRenderer;
import com.grandtheftwarzone.gtwmod.core.display.loadingscreen.listener.ModLoadingListener;
import com.grandtheftwarzone.gtwmod.core.misc.GtwSoundsManager;
import com.grandtheftwarzone.gtwmod.core.phone.core.GtwPhoneGui;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.config.category.ConfigCategory;
import me.phoenixra.atumodcore.api.display.DisplayElement;
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

import java.util.ArrayList;
import java.util.List;

@Mod(modid = GtwProperties.MOD_ID,
        version = GtwProperties.VERSION,
        name = GtwProperties.MOD_NAME)
public class GTWModClient extends AtumMod {
    @Mod.Instance
    public static GTWModClient instance;

    @SidedProxy(clientSide =
            "com.grandtheftwarzone.gtwmod.client.proxy.ClientProxy"
    )
    public static ClientProxy proxy;

    @Getter
    private Config settings;
    @Getter @Setter
    private NetworkManager networkManager;
    @Getter
    private SoundsManager soundsManager;
    @Getter
    private PlayerHUD playerHUD;
    @Getter
    private FactoryGuiHandler factoryGuiHandler;
    @Getter
    private PhoneGui phoneGui;
    @Getter
    private ScreensManager screensManager;
    @Getter
    private PlayerData playerData;


    public GTWModClient(){
        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
            throw new RuntimeException("This mod is client only!");
        }
        instance = this;
        GtwAPI.Instance.set(new GtwAPIClient());
        soundsManager = new GtwSoundsManager();

        settings = getApi().createLoadableConfig(this,
                "settings",
                "",
                ConfigType.JSON,
                false
        );
        registerConfigCategory();

        playerData = new PlayerData();
        playerHUD = new GtwPlayerHUD();
        factoryGuiHandler = new GtwFactoryGuiHandler();
        phoneGui = new GtwPhoneGui();
        screensManager = new GtwScreensManager();
    }
    private void registerConfigCategory(){
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
    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        getConfigManager().reloadAllConfigCategories();
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
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
        //@TODO: move it to the core
        event.getRegistry().registerAll(soundsManager.getAllRegisteredSounds());
    }

    @Mod.EventHandler
    public static void construct(FMLConstructionEvent event) {
        //@TODO: move it to the core
        ModLoadingListener.setup();
        MainSplashRenderer.onReachConstruct();
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
