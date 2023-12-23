package com.grandtheftwarzone.gtwmod.core.display;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.hud.PlayerHUD;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElementType;
import com.grandtheftwarzone.gtwmod.core.display.hud.elements.*;
import com.grandtheftwarzone.gtwmod.core.display.hud.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;


@SideOnly(Side.CLIENT)
public class GtwPlayerHUD implements PlayerHUD {

    public static GtwPlayerHUD instance;
    protected HashMap<HudElementType, HudElement> elements = new HashMap<>();

    protected Minecraft mc;
    public PlayerData playerData;

    public int chatOffset = 0;

    public GtwPlayerHUD() {
        instance = this;
        this.mc = Minecraft.getMinecraft();
        this.setElements();
        playerData = GtwAPI.getInstance().getPlayerData();
    }

    public void setElements() {
        this.elements.put(HudElementType.EXPERIENCE, new HudElementExperience());
        this.elements.put(HudElementType.LEVEL, new HudElementLvl());
        this.elements.put(HudElementType.WIDGET, new HudElementWidget());
        this.elements.put(HudElementType.HEALTH, new HudElementHealth());
        this.elements.put(HudElementType.FOOD, new HudElementFood());
        this.elements.put(HudElementType.ARMOR, new HudElementArmor());
        this.elements.put(HudElementType.MONEY, new HudElementMoney());
        this.elements.put(HudElementType.RANK, new HudElementRank());
        this.elements.put(HudElementType.NOTIFICATION, new HudElementNotification());
        this.elements.put(HudElementType.GANG_PREFIX, new HudElementGangPrefix());
    }

    @Override
    public void draw(Gui gui, int screenWidth, int screenHeight) {
        this.elements.forEach((type, element) -> {
            if (type != HudElementType.NOTIFICATION) {
                GlStateManager.pushMatrix();

                element.draw(gui, screenWidth, screenHeight);

                GlStateManager.popMatrix();
            }
        });
        this.elements.get(HudElementType.NOTIFICATION).draw(gui, screenWidth, screenHeight);
    }
}
