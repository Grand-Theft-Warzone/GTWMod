package me.phoenixra.gtwclient.proxy;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.playerhud.listeners.RenderOverlay;
import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.gui.GuiHandler;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.mainmenu.CustomMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
    private static File skinFile;
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


        // Define the file path
        skinFile = new File(modDir, "saved_skin.png");

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
            event.setGui(new CustomMainMenu());
        }
    }
    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event){

        savePlayerSkin(Minecraft.getMinecraft().player.getLocationSkin());
    }

    @SubscribeEvent
    public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        savePlayerData();
    }

    public static void savePlayerSkin(ResourceLocation resourceLocation){
        try {
            if(skinFile.exists()) skinFile.delete();

            // Get the texture data as an input stream
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();

            // Copy the input stream to the head texture file
            FileUtils.copyInputStreamToFile(inputStream, skinFile);

            // Close the input stream
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void bindPlayerSkinTexture(){
        String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
        String skinUrl = String.format("https://crafatar.com/skins/%s.png", uuid);

        try {
            InputStream inputStream = new URL(skinUrl).openStream();
            BufferedImage skinImage = ImageIO.read(inputStream);
            TextureUtil.uploadTextureImage(new DynamicTexture(skinImage).getGlTextureId(), skinImage);
        }catch (Exception e1){
            try {
                BufferedImage skinImage = ImageIO.read(skinFile);
                BufferedImage headImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

                // Extract the head texture from the skin image
                headImage.getGraphics().drawImage(skinImage, 0, 0, 8, 8, 8, 8, 16, 16, null);

                TextureUtil.uploadTextureImage(new DynamicTexture(headImage).getGlTextureId(), headImage);
            }catch (Exception e){
                Minecraft.getMinecraft().getTextureManager().bindTexture(
                        new ResourceLocation("gtwclient:textures/gui/main_menu/default_head.png")
                );
            }
        }

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
