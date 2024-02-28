package com.grandtheftwarzone.gtwmod.core.network.impl.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PacketRequestMap implements IMessage {


    Config config;

    @Override
    public void toBytes(ByteBuf buf) {
        String text = this.config.toString();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        int size = buf.readInt();
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);
        this.config = (Config) GtwAPI.getInstance().getGtwMod().getConfigManager()
                .createConfigFromString(
                        new String(bytes, StandardCharsets.UTF_8),
                        ConfigType.JSON
                );

    }

}
