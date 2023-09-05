package me.phoenixra.gtwclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.misc.crunch.Crunch;
import me.phoenixra.atumodcore.api.tuples.Pair;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import me.phoenixra.gtwclient.api.gui.GuiElementColor;
import me.phoenixra.gtwclient.fml.test.MainSplashRenderer;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Supplier;

@Mod(modid = "gtwclient",
        version = "1.0.0",
        name = "GTWClient")
public class GTWClient extends AtumMod {

    public static final boolean IS_DEV_ENVIRONMENT = false;

    public static Settings settings;

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
            getApi().createLoadableConfig(this,
                    "settings",
                    "",
                    ConfigType.JSON,
                    false
            );
        }
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        resourcesFolder = event.getModConfigurationDirectory();
        settings = new Settings(getClass().getResourceAsStream("/settings.json"));
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
        ModLoadingListener.setup();
        MainSplashRenderer.onReachConstruct();
    }

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull String getModID() {
        return MOD_ID;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    public static class Settings{
        @Getter
        private String serverHost;
        @Getter
        private int serverPort;
        @Getter
        private String discordLink;
        @Getter
        private String websiteLink;

        private JsonObject baseObject;

        private Settings(InputStream stream){
            JsonParser jsonParser = new JsonParser();
            try(JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
                JsonElement jsonElement = jsonParser.parse(reader);

                baseObject = jsonElement.getAsJsonObject();
                String serverAddress = baseObject.getAsJsonPrimitive("serverAddress").getAsString();

                serverHost = serverAddress.split(":")[0];
                serverPort = Integer.parseInt(serverAddress.split(":")[1]);

                discordLink = baseObject.getAsJsonPrimitive("discordLink").getAsString();
                websiteLink = baseObject.getAsJsonPrimitive("websiteLink").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public String getStringValue(String key){
            if(key.contains(".")){
                String[] keys = key.split("\\.");
                JsonObject object = baseObject.getAsJsonObject(keys[0]);
                for(int i = 1; i < keys.length - 1; i++){
                    object = object.getAsJsonObject(keys[i]);
                }
                return object.getAsJsonPrimitive(keys[keys.length - 1]).getAsString();
            }
            return baseObject.getAsJsonPrimitive(key).getAsString();
        }
        public GuiElementColor getColorValue(String key){
            String value = getStringValue(key);
            return GuiElementColor.fromHex(value.replace("#", ""));
        }
        public double getStringEvaluated(String key, List<Pair<String, Supplier<String>>> placeholders){
            String value = getStringValue(key);
            for(Pair<String, Supplier<String>> replacement : placeholders){
                value = StringUtils.replaceFast(
                        value,
                        replacement.getFirst(),
                        replacement.getSecond().get()
                );
            }
            return Crunch.compileExpression(value).evaluate();
        }
    }
}
