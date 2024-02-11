package com.grandtheftwarzone.gtwmod.core.network.impl.gui;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketGUIAction implements IMessage {

    private String playerUUID;
    private int guiID;
    private int action;
    public PacketGUIAction(){
    }
    public PacketGUIAction(String playerUUID, int guiID, int action){
        this.playerUUID = playerUUID;
        this.guiID = guiID;
        this.action = action;
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(guiID);
        buf.writeInt(action);

        byte[] bytes = playerUUID.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        guiID = buf.readInt();
        action = buf.readInt();

        int textSize = buf.readInt();
        byte[] bytes = new byte[textSize];
        buf.readBytes(bytes);
        playerUUID = new String(bytes, StandardCharsets.UTF_8);

    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public int getAction() {
        return action;
    }

    public int getGuiID() {
        return guiID;
    }
}
