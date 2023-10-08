package com.grandtheftwarzone.gtwclient.core.display.hud;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum HudElementType {
    HEALTH,
    FOOD,
    EXPERIENCE,
    ARMOR,
    WIDGET,
    MONEY,
    RANK,
    GANG_PREFIX,
    NOTIFICATION,
    LEVEL;


}
