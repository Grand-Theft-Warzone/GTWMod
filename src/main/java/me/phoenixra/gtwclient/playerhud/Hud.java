package me.phoenixra.gtwclient.playerhud;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.elements.*;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class Hud {

    public static Hud instance;
    protected HashMap<HudElementType, HudElement> elements = new HashMap<>();

    protected Minecraft mc;
    public PlayerData playerData;

    public int chatOffset = 0;
    public Hud(Minecraft mc) {
        instance = this;
        this.mc = mc;
        this.setElements();
        playerData = ClientProxy.playerData;
    }

    public void setElements() {
        this.elements.put(HudElementType.EXPERIENCE, new HudElementExperience());
        this.elements.put(HudElementType.LEVEL, new HudElementLvl());
        this.elements.put(HudElementType.WIDGET,new HudElementWidget());
        this.elements.put(HudElementType.HEALTH, new HudElementHealth());
        this.elements.put(HudElementType.FOOD, new HudElementFood());
        this.elements.put(HudElementType.MONEY, new HudElementMoney());
        this.elements.put(HudElementType.RANK, new HudElementRank());
        this.elements.put(HudElementType.NOTIFICATION,new HudElementNotification());
    }


    public void drawElement(HudElementType type, Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        this.elements.get(type).draw(gui, zLevel, partialTicks, scaledWidth, scaledHeight);
    }
}
