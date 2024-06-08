package com.grandtheftwarzone.gtwmod.core.network.impl.map.packet;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
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
public class PacketMapRequest implements IMessage {


    protected Config config;

    @Override
    public void toBytes(ByteBuf buf) {
        String text = this.config.toPlaintext();
        GtwLog.getLogger().debug("toBytes: " + text);
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        int textSize = buf.readInt();
        byte[] bytes = new byte[textSize];
        buf.readBytes(bytes);
        String text = new String(bytes, StandardCharsets.UTF_8);
        GtwLog.getLogger().debug("fromBytes: " + text);
        this.config = GtwAPI.getInstance().getGtwMod().getConfigManager().createConfigFromString(text, ConfigType.JSON);
    }

}
