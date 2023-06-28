package me.phoenixra.gtwclient.proxy;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.networking.mainmenu.SocketConnectorPlayerInfo;
import me.phoenixra.gtwclient.playerhud.listeners.RenderOverlay;
import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.gui.GuiHandler;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.mainmenu.CustomMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy{

    public static PlayerData playerData = new PlayerData();

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new Hud(Minecraft.getMinecraft());
        GuiHandler guiHandler = new GuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(
                GTWClient.instance,
                guiHandler
        );
    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new SocketConnectorPlayerInfo(
                GTWClient.settings.getPlayerInfoHost(),
                GTWClient.settings.getPlayerInfoPort()
        ).sendAndRead(Minecraft.getMinecraft().getSession().getProfile().getName());
        new RenderOverlay();

    }
    @SubscribeEvent
    public void onRenderOverlay(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenu){
            event.setGui(new CustomMainMenu());
        }
    }

}
