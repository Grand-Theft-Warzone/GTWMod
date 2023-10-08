package com.grandtheftwarzone.gtwclient.core.display;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import com.grandtheftwarzone.gtwclient.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwclient.api.gui.GuiSession;
import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.core.display.gui.GuiID;
import com.grandtheftwarzone.gtwclient.core.display.gui.factory.FactoryGUI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(value = Side.CLIENT)
public class GtwFactoryGuiHandler implements FactoryGuiHandler {


    @Getter @Setter
    private GuiScreen currentGui;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiID.FACTORY) return getCurrentGui();
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiID.FACTORY) return getCurrentGui();
        return null;
    }

    public void openFactoryGui(String factoryType, GuiSession guiSession){
        setCurrentGui(new FactoryGUI(factoryType,guiSession));
        Minecraft.getMinecraft().player.openGui(GtwAPI.getInstance().getGtwMod(),
                GuiID.FACTORY,
                Minecraft.getMinecraft().player.world,
                0,
                0,
                0
        );
    }

    public void closeGUI(){
        Minecraft.getMinecraft().displayGuiScreen(null);
    }




}
