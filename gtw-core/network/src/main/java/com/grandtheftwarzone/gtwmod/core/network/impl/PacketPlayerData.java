package com.grandtheftwarzone.gtwmod.core.network.impl;

import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketPlayerData implements IMessage {

    protected int level;
    protected double experience;
    protected double experienceCap;
    protected double money;

    protected String rank = "null";
    protected String gang = "null";

    protected String other = "null";


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
    public PacketPlayerData(int level,
                            double experience,
                            double experienceCap,
                            double money,
                            String rank,
                            String gang){
        this.level=level;
        this.experience=experience;
        this.experienceCap =experienceCap;
        this.money = money;
        this.rank = rank;
        this.gang = gang;
    }
    public PacketPlayerData(PlayerData pd){
        this.level=pd.getLevel();
        this.experience=pd.getExperience();
        this.experienceCap =pd.getExperienceCap();
        this.money = pd.getMoney();
        this.rank = pd.getRank();
        this.gang = pd.getGang();
        this.other = pd.parseOtherDataToString();

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

        bytes = gang.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        bytes = other.getBytes(StandardCharsets.UTF_8);
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

        int gangSize = buf.readInt();
        bytes = new byte[gangSize];
        buf.readBytes(bytes);
        gang = new String(bytes, StandardCharsets.UTF_8);

        int otherSize = buf.readInt();
        bytes = new byte[otherSize];
        buf.readBytes(bytes);
        other = new String(bytes, StandardCharsets.UTF_8);
    }
}
