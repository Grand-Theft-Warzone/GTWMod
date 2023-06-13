package me.phoenixra.playerhud.hud;

import net.minecraft.client.resources.I18n;
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
