package com.grandtheftwarzone.gtwclient.core.network.impl.gui;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import com.grandtheftwarzone.gtwclient.api.gui.GuiSession;
import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.core.display.gui.GuiID;
import com.grandtheftwarzone.gtwclient.core.display.gui.api.BaseGUI;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerFactoryGUI implements IMessageHandler<PacketFactoryGUI, IMessage> {

    @Override
    public IMessage onMessage(PacketFactoryGUI message, MessageContext ctx) {
        PlayerData pd = GtwAPI.getInstance().getPlayerData();

        BaseGUI gui =  (BaseGUI) GtwAPI.getInstance().getFactoryGuiHandler().getCurrentGui();

        if(message.actionType==-1){
            if(gui!=null && gui.getId() == GuiID.FACTORY){
                GtwAPI.getInstance().getFactoryGuiHandler().closeGUI();
            }
            return null;
        }


        boolean update = false;
        GuiSession session = (gui!=null && gui.getId() == GuiID.FACTORY) ? gui.getGuiSession() : new GuiSession();

        session.setData("factoryOwner", StringUtils.format(message.factoryOwner));
        session.setData("level",StringUtils.format(message.level));
        session.setData("productionInfo",StringUtils.format(message.productionInfo));
        session.setData("storageInfo",StringUtils.format(message.storageInfo));
        session.setData("productionEfficiency",StringUtils.format(message.productionEfficiency));
        session.setData("storageEfficiency",StringUtils.format(message.storageEfficiency));
        session.setData("productionUpgradePrice",StringUtils.format(message.productionUpgradePrice));
        session.setData("storageUpgradePrice",StringUtils.format(message.storageUpgradePrice));

        String s = session.getData("upgradeDelay");
        if(s!=null) {
            double oldDelay = Double.parseDouble(s);
            update = (oldDelay == 0 && message.upgradeDelay != 0)
                    ||
                    (oldDelay != 0 && message.upgradeDelay == 0);
        }
        session.setData("upgradeDelay", String.valueOf(message.upgradeDelay));

        if(gui==null||gui.getId() != GuiID.FACTORY){
            GtwAPI.getInstance().getFactoryGuiHandler().openFactoryGui(message.factoryType,
                    session);
        }else if(update){
            gui.reload();
        }
        return null;
    }
}
