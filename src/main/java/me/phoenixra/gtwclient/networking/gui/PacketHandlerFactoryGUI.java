package me.phoenixra.gtwclient.networking.gui;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.gui.GuiHandler;
import me.phoenixra.gtwclient.gui.GuiID;
import me.phoenixra.gtwclient.gui.GuiSession;
import me.phoenixra.gtwclient.gui.api.BaseGUI;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerFactoryGUI implements IMessageHandler<PacketFactoryGUI, IMessage> {

    @Override
    public IMessage onMessage(PacketFactoryGUI message, MessageContext ctx) {

        PlayerData pd = ClientProxy.playerData;

        BaseGUI gui =  pd.getOpenedGui();

        if(message.actionType==-1){
            if(gui!=null && gui.getId() == GuiID.FACTORY){
                GuiHandler.closeGUI();
            }
            return null;
        }


        boolean update = false;
        GuiSession session = (gui!=null && gui.getId() == GuiID.FACTORY) ? gui.getGuiSession() : new GuiSession();

        session.setData("factoryOwner",message.factoryOwner.replace("&","\u00a7"));
        session.setData("level",message.level.replace("&","\u00a7"));
        session.setData("productionInfo",message.productionInfo.replace("&","\u00a7"));
        session.setData("storageInfo",message.storageInfo.replace("&","\u00a7"));
        session.setData("productionEfficiency",message.productionEfficiency.replace("&","\u00a7"));
        session.setData("storageEfficiency",message.storageEfficiency.replace("&","\u00a7"));
        session.setData("productionUpgradePrice",message.productionUpgradePrice.replace("&","\u00a7"));
        session.setData("storageUpgradePrice",message.storageUpgradePrice.replace("&","\u00a7"));

        String s = session.getData("upgradeDelay");
        if(s!=null) {
            double oldDelay = Double.parseDouble(s);
            update = (oldDelay == 0 && message.upgradeDelay != 0)
                    ||
                    (oldDelay != 0 && message.upgradeDelay == 0);
        }
        session.setData("upgradeDelay", message.upgradeDelay+"");

        if(gui==null||gui.getId() != GuiID.FACTORY){
            GuiHandler.openFactoryGui(message.factoryType,
                    session);
        }else if(update){
            gui.reload();
        }
        return null;
    }
}
