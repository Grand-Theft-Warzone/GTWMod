package me.phoenixra.gtwclient.proxy;

import me.phoenixra.atumodcore.api.placeholders.types.SimplePlaceholder;
import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.playerhud.listeners.RenderOverlay;
import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.gui.GuiHandler;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.gui.types.CustomMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClientProxy extends CommonProxy{

    public static PlayerData playerData = new PlayerData();
    private static File dataFile;
    private static ISound theme_music;

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new Hud(Minecraft.getMinecraft());
        GuiHandler guiHandler = new GuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(
                GTWClient.instance,
                guiHandler
        );
        // Get the Minecraft directory
        File minecraftDir = event.getModConfigurationDirectory().getParentFile();

        // Create a subdirectory for your mod's data
        File modDir = new File(minecraftDir, "gtwdata");
        if (!modDir.exists()) {
            modDir.mkdirs();
        }

        // Define the file path
        dataFile = new File(modDir, "data.txt");

        loadPlayerData();

    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new SimplePlaceholder(GTWClient.instance,"player_kills",
                ()->playerData.getOtherOrDefault("kills","0")
        ).register();
        new SimplePlaceholder(GTWClient.instance,"player_deaths",
                ()->playerData.getOtherOrDefault("deaths","0")
        ).register();
        new SimplePlaceholder(GTWClient.instance,"player_name",
                ()-> Minecraft.getMinecraft().getSession().getUsername()
        ).register();
        new SimplePlaceholder(GTWClient.instance,"player_money",
                ()-> String.valueOf(playerData.getMoney())
        ).register();
        new SimplePlaceholder(GTWClient.instance,"player_level",
                ()-> String.valueOf(playerData.getLevel())
        ).register();
        new SimplePlaceholder(GTWClient.instance,"player_rank",
                ()-> playerData.getRank()
        ).register();
        new SimplePlaceholder(GTWClient.instance,"player_gang",
                ()-> playerData.getGang()
        ).register();


        new RenderOverlay();
    }

    @SubscribeEvent
    public void onRenderOverlay(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenu){
            event.setGui(new CustomMainMenu());
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        savePlayerData();
    }
    public static void savePlayerData(){
        // Save data to the file
        try {
            if(dataFile.exists()) dataFile.delete();

            FileUtils.writeStringToFile(dataFile, playerData.toPlainText(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadPlayerData(){
        // Load data from the file
        try {
            String loadedData = FileUtils.readFileToString(dataFile, StandardCharsets.UTF_8);
            playerData.fromString(loadedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
