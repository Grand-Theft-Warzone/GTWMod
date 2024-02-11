package com.grandtheftwarzone.gtwmod.core.network.impl.gui;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketFactoryGUI implements IMessage {

    protected int actionType;

    protected String factoryType;

    protected String factoryOwner;
    protected String level;

    protected String productionInfo;
    protected String storageInfo;
    protected String productionEfficiency;
    protected String storageEfficiency;

    protected String productionUpgradePrice;
    protected String storageUpgradePrice;

    protected double upgradeDelay;

    public PacketFactoryGUI(){

    }
    public PacketFactoryGUI(String factoryType,
                            String factoryOwner,
                            String level,
                            String productionInfo,
                            String storageInfo,
                            String productionEfficiency,
                            String storageEfficiency,
                            String productionUpgradePrice,
                            String storageUpgradePrice,
                            double upgradeDelay
    ){

        this.actionType = 0;
        this.level = level;

        this.productionInfo = productionInfo;
        this.storageInfo = storageInfo;
        this.productionEfficiency = productionEfficiency;
        this.storageEfficiency = storageEfficiency;

        this.productionUpgradePrice = productionUpgradePrice;
        this.storageUpgradePrice = storageUpgradePrice;


        this.upgradeDelay = upgradeDelay;

        this.factoryOwner = factoryOwner;
        this.factoryType = factoryType;

    }
    public PacketFactoryGUI(String factoryType,
                            String factoryOwner,
                            String level,
                            String productionInfo,
                            String storageInfo,
                            String productionEfficiency,
                            String storageEfficiency,
                            String productionUpgradePrice,
                            String storageUpgradePrice,
                            double upgradeDelay,
                            int actionType
    ){

        this.actionType = actionType;
        this.level = level;

        this.productionInfo = productionInfo;
        this.storageInfo = storageInfo;
        this.productionEfficiency = productionEfficiency;
        this.storageEfficiency = storageEfficiency;

        this.productionUpgradePrice = productionUpgradePrice;
        this.storageUpgradePrice = storageUpgradePrice;

        this.upgradeDelay = upgradeDelay;

        this.factoryOwner = factoryOwner;
        this.factoryType = factoryType;

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(actionType);
        if(actionType==-1){
            return;
        }

        buf.writeDouble(upgradeDelay);

        byte[] bytes = factoryOwner.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = factoryType.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = level.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = productionInfo.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = storageInfo.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = productionEfficiency.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = storageEfficiency.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        bytes = productionUpgradePrice.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        bytes = storageUpgradePrice.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        actionType = buf.readInt();
        if(actionType==-1){
            return;
        }

        upgradeDelay = buf.readDouble();

        int textSize = buf.readInt();
        byte[] bytes = new byte[textSize];
        buf.readBytes(bytes);
        factoryOwner = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        factoryType = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        level = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        productionInfo = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        storageInfo = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        productionEfficiency = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        storageEfficiency = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        productionUpgradePrice = new String(bytes, StandardCharsets.UTF_8);

        textSize = buf.readInt();
        bytes = new byte[textSize];
        buf.readBytes(bytes);
        storageUpgradePrice = new String(bytes, StandardCharsets.UTF_8);
    }


}
