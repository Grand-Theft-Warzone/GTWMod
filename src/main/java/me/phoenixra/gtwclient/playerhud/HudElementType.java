package me.phoenixra.gtwclient.playerhud;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum HudElementType {
    HEALTH,
    FOOD,
    EXPERIENCE,
    WIDGET,
    MONEY,
    RANK,
    NOTIFICATION,
    LEVEL;


}
