package me.phoenixra.playerhud.hud.elements;

import me.phoenixra.playerhud.data.PlayerData;
import me.phoenixra.playerhud.hud.Hud;
import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.hud.HudElementType;
import me.phoenixra.playerhud.hud.notification.NotificationRequest;
import me.phoenixra.playerhud.sounds.SoundsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.SoundCategory;

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
