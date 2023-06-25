package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import me.phoenixra.gtwclient.playerhud.notification.NotificationRequest;
import me.phoenixra.gtwclient.sounds.SoundsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudElementNotification extends HudElement {
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

        GlStateManager.pushMatrix();
        GlStateManager.translate(
                scaledWidth / notification.getSizeX(),
                scaledHeight / notification.getSizeY(),
                0
        );
        GlStateManager.scale(
                notification.getSizeX(),
                notification.getSizeY(),
                1
        );
        mc.fontRenderer.drawStringWithShadow(
                notification.getText(),
                notification.getPositionX(),
                notification.getPositionY(),
                0xFFFFFF
        );
        GlStateManager.popMatrix();
    }
}
