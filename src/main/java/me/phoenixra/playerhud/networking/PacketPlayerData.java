package me.phoenixra.playerhud.networking;

import io.netty.buffer.ByteBuf;
import me.phoenixra.playerhud.data.PlayerData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketPlayerData implements IMessage {

    protected int level;
    protected double experience;
    protected double experienceCap;
    protected double money;

    protected String rank;

    public PacketPlayerData(){
        this.level=1;
        this.experience=0;
        this.experienceCap =1;
    }
    public PacketPlayerData(int level,
                            double experience,
                            double experienceCap,
                            double money,
                            String rank){
        this.level=level;
        this.experience=experience;
        this.experienceCap =experienceCap;
        this.money = money;
        this.rank = rank;
    }
    public PacketPlayerData(PlayerData pd){
        this.level=pd.getLevel();
        this.experience=pd.getExperience();
        this.experienceCap =pd.getExperienceCap();
        this.money = pd.getMoney();
        this.rank = pd.getRank();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(level);
        buf.writeDouble(experience);
        buf.writeDouble(experienceCap);
        buf.writeDouble(money);

        byte[] bytes = rank.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        level = buf.readInt();
        experience = buf.readDouble();
        experienceCap = buf.readDouble();
        money = buf.readDouble();

        int rankSize = buf.readInt();
        byte[] bytes = new byte[rankSize];
        buf.readBytes(bytes);
        rank = new String(bytes, StandardCharsets.UTF_8);
    }
}
