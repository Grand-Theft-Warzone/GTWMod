package com.grandtheftwarzone.gtwclient.core.network.impl;

import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.core.display.GtwPlayerHUD;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerPlayerData implements IMessageHandler<PacketPlayerData, IMessage> {
    @Override
    public IMessage onMessage(PacketPlayerData message, MessageContext ctx) {
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        if(message.level>0) {
            pd.setLevel(message.level);
        }
        if(message.experience>0) {
            pd.setExperience(message.experience);
        }
        if(message.experienceCap>0) {
            pd.setExperienceCap(message.experienceCap);
        }
        if(message.money!=-0.0001) {
            pd.setMoney(message.money);
        }
        if(!message.rank.equals("null")) {
            pd.setRank(message.rank);
        }
        if(!message.gang.equals("null")) {
            pd.setGang(message.gang);
        }
        return null;
    }
}
