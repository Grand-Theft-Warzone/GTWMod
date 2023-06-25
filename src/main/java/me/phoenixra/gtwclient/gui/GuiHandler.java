package me.phoenixra.gtwclient.gui;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.gui.factory.FactoryGUI;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(value = Side.CLIENT)
public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiID.FACTORY) return ClientProxy.playerData.getOpenedGui();
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiID.FACTORY) return ClientProxy.playerData.getOpenedGui();
        return null;
    }

    public static void openFactoryGui(String factoryType, GuiSession guiSession){
        ClientProxy.playerData.setOpenedGui(new FactoryGUI(factoryType,guiSession));
        Minecraft.getMinecraft().player.openGui(GTWClient.instance,
                GuiID.FACTORY,
                Minecraft.getMinecraft().player.world,
                0,
                0,
                0
        );
    }

    public static void closeGUI(){
        Minecraft.getMinecraft().displayGuiScreen(null);
    }




}
