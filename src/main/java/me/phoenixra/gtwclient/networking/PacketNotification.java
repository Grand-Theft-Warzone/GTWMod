package me.phoenixra.gtwclient.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
public class PacketNotification implements IMessage {
    protected String text;
    protected long displayTime;

    protected float positionX;
    protected float positionY;

    protected double sizeX;
    protected double sizeY;

    protected boolean playSound;

    public PacketNotification(){
        this.displayTime=1;
        this.text = "test";
        positionX = 0;
        positionY = 0;
        sizeX = 1;
        sizeY = 1;
    }
    public PacketNotification(String text,
                              boolean playSound,
                              long displayTime,
                              float positionX,
                              float positionY,
                              double sizeX,
                              double sizeY
    ){
        this.text = text;
        this.displayTime = displayTime;

        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.playSound = playSound;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(positionX);
        buf.writeFloat(positionY);
        buf.writeDouble(sizeX);
        buf.writeDouble(sizeY);

        buf.writeLong(displayTime);

        buf.writeBoolean(playSound);

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        positionX = buf.readFloat();
        positionY = buf.readFloat();
        sizeX = buf.readDouble();
        sizeY = buf.readDouble();

        displayTime = buf.readLong();

        playSound = buf.readBoolean();

        int textSize = buf.readInt();
        byte[] bytes = new byte[textSize];
        buf.readBytes(bytes);
        text = new String(bytes, StandardCharsets.UTF_8);
    }
}
