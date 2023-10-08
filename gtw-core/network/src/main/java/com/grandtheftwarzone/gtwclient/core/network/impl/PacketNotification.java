package com.grandtheftwarzone.gtwclient.core.network.impl;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
public class PacketNotification implements IMessage {
    protected String text;
    protected long displayTime;

    protected int positionX;
    protected int positionY;

    protected int fontSize;

    protected boolean playSound;

    public PacketNotification(){
        this.displayTime=1;
        this.text = "test";
        positionX = 0;
        positionY = 0;
        fontSize = 0;
    }
    public PacketNotification(String text,
                              boolean playSound,
                              long displayTime,
                              int positionX,
                              int positionY,
                              int fontSize
    ){
        this.text = text;
        this.displayTime = displayTime;

        this.positionX = positionX;
        this.positionY = positionY;
        this.fontSize = fontSize;

        this.playSound = playSound;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(positionX);
        buf.writeInt(positionY);
        buf.writeInt(fontSize);

        buf.writeLong(displayTime);

        buf.writeBoolean(playSound);

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        positionX = buf.readInt();
        positionY = buf.readInt();
        fontSize = buf.readInt();

        displayTime = buf.readLong();

        playSound = buf.readBoolean();

        int textSize = buf.readInt();
        byte[] bytes = new byte[textSize];
        buf.readBytes(bytes);
        text = new String(bytes, StandardCharsets.UTF_8);
    }
}
