package com.grandtheftwarzone.gtwclient.core.minimap.renderer;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import com.grandtheftwarzone.gtwclient.core.minimap.utils.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class MinimapRenderer {

    public void draw() {
        GlStateManager.pushMatrix();

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float y = scaledResolution.getScaledHeight() - GTWMinimap.getInstance().getCornerDistance() - (GTWMinimap.getInstance().getRadius() * 2);
        GlStateManager.translate(GTWMinimap.getInstance().getCornerDistance(), y, 1);

        drawMap(scaledResolution.getScaleFactor());
        drawPlayer();

        GlStateManager.popMatrix();
    }


    private void drawMap(float scaleFactor) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

//        GlStateManager.enableTexture2D();
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        //TODO: Scale according to screen size
//        GlStateManager.scale(scaleFactor, scaleFactor, 1);

        if (GTWMinimap.getInstance().isRotating()) {
            float angle = 180f - Minecraft.getMinecraft().player.rotationYaw;
            GLUtils.rotateFixed(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), angle);
        }

        GLUtils.enableCircleStencil(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius());

        GlStateManager.bindTexture(GTWMinimap.getInstance().getMapTexture().getId());

        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, GTWMinimap.getInstance().getRadius() * 2, GTWMinimap.getInstance().getRadius() * 2, GTWMinimap.getInstance().getRadius() * 2, GTWMinimap.getInstance().getRadius() * 2);

        GLUtils.disableStencil();

        GlStateManager.color(0.2f, 0.2f, 0.2f, 1f);
        GLUtils.drawCircle(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), 2);

        GlStateManager.popMatrix();
    }

    private void drawPlayer() {
    }
}
