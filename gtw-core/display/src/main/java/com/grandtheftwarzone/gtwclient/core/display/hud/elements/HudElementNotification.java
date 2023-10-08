package com.grandtheftwarzone.gtwclient.core.display.hud.elements;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import com.grandtheftwarzone.gtwclient.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwclient.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwclient.core.display.hud.HudElementType;
import me.phoenixra.atumodcore.api.display.font.DisplayFont;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.HashMap;

public class HudElementNotification extends HudElement {
    private HashMap<Integer, DisplayFont> fonts = new HashMap<>();
    public HudElementNotification() {
        super(HudElementType.MONEY, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }
    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        NotificationRequest notification = GtwPlayerHUD.instance.playerData.getQueuedNotification();
        if(notification==null) return;
        if(!notification.isStarted()){
            notification.start();
            if(notification.isPlaySound()) {
                Minecraft.getMinecraft().player.playSound(GtwAPI.getInstance()
                                .getSoundsManager().getSound("levelup"),
                        1.0f, 1.0f);
            }

        }else{
            if(notification.isFinished()){
                notification = GtwPlayerHUD.instance.playerData.nextQueuedNotification();
                if(notification==null) return;
            }
        }
        int[] coords = RenderUtils.fixCoordinates(notification.getPositionX(),
                notification.getPositionY());
        int fontSize = notification.getFontSize();
        DisplayFont font = fonts.get(fontSize);
        if(font==null){
            font = new DisplayFont(new Font("Arial", Font.BOLD, fontSize));
            fonts.put(fontSize, font);
        }
        font.drawString(
                notification.getText(),
                coords[0],
                coords[1],
                AtumColor.BLACK
        );
    }
}
