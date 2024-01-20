package com.grandtheftwarzone.gtwclient.core.minimap.renderer;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import com.grandtheftwarzone.gtwclient.core.minimap.utils.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import java.awt.*;
import java.util.List;

public class MinimapRenderer {


    public void draw() {
        GlStateManager.pushMatrix();

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float y = scaledResolution.getScaledHeight() - GTWMinimap.getInstance().getCornerDistance() - (GTWMinimap.getInstance().getRadius() * 2);

        //TODO: Scale according to screen size
        // GlStateManager.scale(0.5, 0.5, 0.5);

        GlStateManager.translate(GTWMinimap.getInstance().getCornerDistance(), y, 1);

        drawMap();
        drawMarkers();
        drawPlayer();

        //Draw border
        GlStateManager.color(0.2f, 0.2f, 0.2f, 1f);
        GLUtils.drawCircle(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), 2);

        GlStateManager.popMatrix();
    }

    private void drawMap() {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

        if (GTWMinimap.getInstance().isRotating()) {
            float angle = -Minecraft.getMinecraft().player.rotationYaw;
            GLUtils.rotateFixed(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), angle);
        }

        GlStateManager.bindTexture(GTWMinimap.getInstance().getMapTexture().getId());
        GlStateManager.enableTexture2D();
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GLUtils.enableCircleStencil(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius());

        double mapSize = GTWMinimap.getInstance().getRadius() * 2;
        double textureSize = GTWMinimap.getInstance().getTextureSize();

        int mapCenterX = GTWMinimap.getInstance().getMinimap().getCenterX();
        int mapCenterY = GTWMinimap.getInstance().getMinimap().getCenterZ();

        double ratio = mapSize / textureSize;

        EntityPlayer player = Minecraft.getMinecraft().player;

        double subBlockX = mapCenterX - player.posX;
        double subBlockZ = mapCenterY - player.posZ;

        double centerX = 0.5 + (subBlockX / textureSize);
        double centerY = 0.5 + (subBlockZ / textureSize);

        double uMin = centerX - (ratio / 2);
        double uMax = centerX + (ratio / 2);
        double vMin = centerY - (ratio / 2);
        double vMax = centerY + (ratio / 2);

        drawTexturedRect(0, 0, mapSize, mapSize, uMin, vMin, uMax, vMax);


        GLUtils.disableStencil(); // Disable clipping

        GlStateManager.popMatrix();
    }

    private void drawPlayer() {
        GlStateManager.color(1, 0, 0, 1);

        GLUtils.drawCircle(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), 2, true);
    }

    private void drawMarkers() {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

        List<Marker> markers = GTWMinimap
                .getInstance()
                .getClientMarkerManager()
                .getMinimapMarkers(GTWMinimap.getInstance().getMinimap());

        float angle = 180 - Minecraft.getMinecraft().player.rotationYaw;

        if (GTWMinimap.getInstance().isRotating())
            GLUtils.rotateFixed(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), angle);

        GLUtils.enableCircleStencil(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius());

        double playerX = Minecraft.getMinecraft().player.posX;
        double playerZ = Minecraft.getMinecraft().player.posZ;

        for (Marker marker : markers) {
            GlStateManager.pushMatrix();

            double offsetX = marker.getPosX() - playerX;
            double offsetY = marker.getPosZ() - playerZ;

            double x = GTWMinimap.getInstance().getRadius() + offsetX;
            double y = GTWMinimap.getInstance().getRadius() + offsetY;

            boolean overlap = GTWMinimap.getInstance().getClientMarkerManager().hasMarkerOverlap(marker);
            if (overlap) GlStateManager.color(1f, 1f, 1f, 0.25f);

            ResourceLocation texture = marker.getType().getTexture();
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

            double uMin = marker.getType().getAtlas().getUMin();
            double uMax = marker.getType().getAtlas().getUMax();
            double vMin = marker.getType().getAtlas().getVMin();
            double vMax = marker.getType().getAtlas().getVMax();

            double width = marker.getType().getAtlas().getWidth();
            double height = marker.getType().getAtlas().getHeight();

            x -= width / 2d;
            y -= height / 2d;

            if (GTWMinimap.getInstance().isRotating())
                GLUtils.rotateFixed(x + width / 2f, y + width / 2f, -angle); // Rotate marker to always face the player

            drawTexturedRect(x, y, width, height, uMin, vMin, uMax, vMax);

            GlStateManager.popMatrix();
        }

        GLUtils.disableStencil(); // Disable clipping
        GlStateManager.popMatrix();
    }

    public static void drawTexturedRect(double x, double y, double w, double h, double u1, double v1, double u2, double v2) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x + w, y, -1).tex(u2, v1).endVertex();
        vertexbuffer.pos(x, y, -1).tex(u1, v1).endVertex();
        vertexbuffer.pos(x, y + h, -1).tex(u1, v2).endVertex();
        vertexbuffer.pos(x + w, y + h, -1).tex(u2, v2).endVertex();
        // renderer.finishDrawing();
        tessellator.draw();
        GlStateManager.disableBlend();
    }


}
