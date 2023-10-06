package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.atumodcore.api.display.font.DisplayFont;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import me.phoenixra.gtwclient.playerhud.notification.NotificationRequest;
import me.phoenixra.gtwclient.sounds.SoundsHandler;
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
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        NotificationRequest notification = Hud.instance.playerData.getQueuedNotification();
        if(notification==null) return;
        if(!notification.isStarted()){
            notification.start();
            if(notification.isPlaySound()) {
                Minecraft.getMinecraft().player.playSound(SoundsHandler.USER_LEVEL_UP,
                        1.0f, 1.0f);
            }

        }else{
            if(notification.isFinished()){
                notification = Hud.instance.playerData.nextQueuedNotification();
                if(notification==null) return;
            }
        }
        GlStateManager.disableBlend();
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
        GlStateManager.enableBlend();
    }
}
