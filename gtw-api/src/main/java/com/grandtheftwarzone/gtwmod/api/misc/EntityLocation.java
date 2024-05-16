package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

public class EntityLocation {

    @Getter @Setter
    private double x, y, z;

    @Getter @Setter
    private float yaw, pitch;

    public EntityLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public EntityLocation() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.yaw = 0;
        this.pitch = 0;
    }

    public EntityLocation(String position) {
        String[] split = position.split(";");
        this.x = Double.parseDouble(split[0]);
        this.y = Double.parseDouble(split[1]);
        this.z = Double.parseDouble(split[2]);
        this.yaw = Float.parseFloat(split[3]);
        this.pitch = Float.parseFloat(split[4]);
    }

    public EntityLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    public EntityLocation(EntityPlayer player) {
        if (player != null) {
            this.x = player.posX;
            this.y = player.posY;
            this.z = player.posZ;
            this.yaw = player.rotationYaw;
            this.pitch = player.rotationPitch;
        } else {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.yaw = 0;
            this.pitch = 0;
        }
    }

    public EntityLocation update(EntityPlayer player) {
        this.x = player.posX;
        this.y = player.posY;
        this.z = player.posZ;
        this.yaw = player.rotationYaw;
        this.pitch = player.rotationPitch;

        return this;
    }

    public String toString() {
        return x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
    }

}
