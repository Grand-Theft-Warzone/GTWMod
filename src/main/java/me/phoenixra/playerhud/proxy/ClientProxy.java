package me.phoenixra.playerhud.proxy;

import me.phoenixra.playerhud.PlayerHUD;
import me.phoenixra.playerhud.RenderOverlay;
import me.phoenixra.playerhud.data.PlayerData;
import me.phoenixra.playerhud.gui.GuiHandler;
import me.phoenixra.playerhud.hud.Hud;
import me.phoenixra.playerhud.sounds.SoundsHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy{

    public static PlayerData playerData = new PlayerData();

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new Hud(Minecraft.getMinecraft());
        GuiHandler guiHandler = new GuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(
                PlayerHUD.instance,
                guiHandler
        );
    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new RenderOverlay();

    }

}
