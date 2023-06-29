package me.phoenixra.gtwclient.proxy;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.playerhud.listeners.RenderOverlay;
import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.gui.GuiHandler;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.mainmenu.CustomMainMenu;
import me.phoenixra.gtwclient.sounds.SoundsHandler;
import me.phoenixra.gtwclient.utils.PrivateFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        new RenderOverlay();

    }


    @SubscribeEvent
    public void onRenderOverlay(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenu){
            //sound
            SoundEventAccessor accessor = Minecraft.getMinecraft().getSoundHandler()
                    .getAccessor(Minecraft.getMinecraft().getAmbientMusicType().getMusicLocation().getSoundName());
            if (accessor != null) ((List) PrivateFields.eventSounds.get(accessor)).clear();

            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
            theme_music = PositionedSoundRecord.getMusicRecord(SoundsHandler.MAIN_MENU_THEME);
            soundHandler.playSound(theme_music);




            event.setGui(new CustomMainMenu());
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        savePlayerData();
    }

    @SubscribeEvent
    public void onPlayerJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event){
        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        soundHandler.stopSound(theme_music);
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
