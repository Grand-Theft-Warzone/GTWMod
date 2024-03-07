package com.grandtheftwarzone.gtwmod.core.network.impl.minimap;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PacketTest implements IMessage {

    protected String text;

    @Override
    public void fromBytes(ByteBuf buf) {
        System.out.println("вызываем fromBypes");
        int textSize = buf.readInt();
        byte[] bytes = new byte[textSize];
        buf.readBytes(bytes);
        text = new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        System.out.println("вызываем toBypes");
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }
}
