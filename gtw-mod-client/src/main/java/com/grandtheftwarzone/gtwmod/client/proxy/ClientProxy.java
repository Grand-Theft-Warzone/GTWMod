package com.grandtheftwarzone.gtwmod.client.proxy;

import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.client.GTWModClient;
import com.grandtheftwarzone.gtwmod.core.display.hud.render.GtwHudRenderer;
import com.grandtheftwarzone.gtwmod.core.network.GtwNetworkAPI;
import me.phoenixra.atumodcore.api.placeholders.types.SimplePlaceholder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
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

public class ClientProxy {

    public static PlayerData playerData = GTWModClient.instance.getPlayerData();

    //@TODO replace it with a config from atumModCore
    private static File dataFile;

    public ClientProxy(){
        MinecraftForge.EVENT_BUS.register(this);
        GTWModClient.instance.setNetworkAPI(new GtwNetworkAPI());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        NetworkRegistry.INSTANCE.registerGuiHandler(
                GTWModClient.instance,
                GTWModClient.instance.getFactoryGuiHandler()
        );
        File minecraftDir = event.getModConfigurationDirectory().getParentFile();

        File modDir = new File(minecraftDir, "gtwdata");
        if (!modDir.exists()) {
            modDir.mkdirs();
        }

        dataFile = new File(modDir, "data.txt");

        loadPlayerData();

    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new SimplePlaceholder(GTWModClient.instance,"player_kills",
                ()->playerData.getOtherOrDefault("kills","0")
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_deaths",
                ()->playerData.getOtherOrDefault("deaths","0")
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_kdr",
                ()->playerData.getOtherOrDefault("kdr","0")
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_name",
                ()-> Minecraft.getMinecraft().getSession().getUsername()
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_money",
                ()-> String.valueOf(playerData.getMoney())
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_level",
                ()-> String.valueOf(playerData.getLevel())
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_rank",
                ()-> playerData.getRank()
        ).register();
        new SimplePlaceholder(GTWModClient.instance,"player_gang",
                ()-> playerData.getGang()
        ).register();

        //@TODO move that to the PlayerHUD for better organization
        new GtwHudRenderer();
    }

    @SubscribeEvent
    public void onRenderOverlay(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenu){
            event.setGui(GTWModClient.instance.getScreensManager().getMainMenuGuiScreen());
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        savePlayerData();
    }
    public static void savePlayerData(){
        try {
            if(dataFile.exists()) dataFile.delete();

            FileUtils.writeStringToFile(dataFile, playerData.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadPlayerData(){
        try {
            String loadedData = FileUtils.readFileToString(dataFile, StandardCharsets.UTF_8);
            playerData.fromString(loadedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
