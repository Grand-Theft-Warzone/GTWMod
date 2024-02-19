package com.grandtheftwarzone.gtwmod.core.network.impl.gui;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.GuiSession;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerFactoryGUI implements IMessageHandler<PacketFactoryGUI, IMessage> {

    @Override
    public IMessage onMessage(PacketFactoryGUI message, MessageContext ctx) {
        PlayerData pd = GtwAPI.getInstance().getPlayerData();

        int guiId =  GtwAPI.getInstance().getFactoryGuiHandler().getGuiID();

        if(message.actionType==-1){
            if(GtwAPI.getInstance().getFactoryGuiHandler().isFactoryGuiOpened()
                    && guiId == 1){
                GtwAPI.getInstance().getFactoryGuiHandler().closeGUI();
            }
            return null;
        }


        boolean update = false;
        GuiSession session = GtwAPI.getInstance().getFactoryGuiHandler().getGuiSession();
        if(session==null){
            session = new GuiSession();
        }

        session.setData("factoryOwner", StringUtils.formatMinecraftColors(message.factoryOwner));
        session.setData("level",StringUtils.formatMinecraftColors(message.level));
        session.setData("productionInfo",StringUtils.formatMinecraftColors(message.productionInfo));
        session.setData("storageInfo",StringUtils.formatMinecraftColors(message.storageInfo));
        session.setData("productionEfficiency",StringUtils.formatMinecraftColors(message.productionEfficiency));
        session.setData("storageEfficiency",StringUtils.formatMinecraftColors(message.storageEfficiency));
        session.setData("productionUpgradePrice",StringUtils.formatMinecraftColors(message.productionUpgradePrice));
        session.setData("storageUpgradePrice",StringUtils.formatMinecraftColors(message.storageUpgradePrice));

        String s = session.getData("upgradeDelay");
        if(s!=null) {
            double oldDelay = Double.parseDouble(s);
            update = (oldDelay == 0 && message.upgradeDelay != 0)
                    ||
                    (oldDelay != 0 && message.upgradeDelay == 0);
        }
        session.setData("upgradeDelay", String.valueOf(message.upgradeDelay));

        if(!GtwAPI.getInstance().getFactoryGuiHandler().isFactoryGuiOpened()
                && guiId != 1){
            GtwAPI.getInstance().getFactoryGuiHandler().openFactoryGui(message.factoryType,
                    session);
        }else if(update){
            GtwAPI.getInstance().getFactoryGuiHandler().reloadGUI();
        }
        return null;
    }
}
