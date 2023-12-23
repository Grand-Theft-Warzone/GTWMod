package com.grandtheftwarzone.gtwmod.core.display;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwmod.api.gui.GuiSession;
import com.grandtheftwarzone.gtwmod.core.display.gui.GuiID;
import com.grandtheftwarzone.gtwmod.core.display.gui.api.BaseGUI;
import com.grandtheftwarzone.gtwmod.core.display.gui.factory.FactoryGUI;
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

    @Override
    public void reloadGUI() {
        if(getCurrentGui()!=null){
            ((BaseGUI)getCurrentGui()).reload();
        }
    }

    @Override
    public boolean isFactoryGuiOpened() {
        return getCurrentGui() != null;
    }

    @Override
    public int getGuiID() {
        return 1;
    }

    @Override
    public GuiSession getGuiSession() {
        return currentGui==null ? null : ((BaseGUI)currentGui).getGuiSession();
    }


}
