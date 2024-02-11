package com.grandtheftwarzone.gtwmod.core.display.minimap;

import com.grandtheftwarzone.gtwmod.api.minimap.Marker;
import com.grandtheftwarzone.gtwmod.core.display.minimap.utils.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;


public class MinimapRenderer {

    public void draw() {
        GlStateManager.pushMatrix();

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float y = scaledResolution.getScaledHeight() - GtwMinimap.getInstance().getCornerDistance() - (GtwMinimap.getInstance().getRadius() * 2);

        //TODO: Scale according to screen size
        // GlStateManager.scale(0.5, 0.5, 0.5);

        GlStateManager.translate(GtwMinimap.getInstance().getCornerDistance(), y, 1);

        drawMap();
        drawMarkers();
        drawPlayer();

        //Draw border
        GlStateManager.color(0.2f, 0.2f, 0.2f, 1f);
        GLUtils.drawCircle(GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), 2);

        GlStateManager.popMatrix();
    }


    private void drawMap() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        TexturedMinimap minimap = GtwMinimap.getInstance().getMinimap();
        double mapSize = GtwMinimap.getInstance().getRadius() * 2;

        int textureWidth = GtwMinimap.getInstance().getMinimap().getWidth();
        int textureHeight = GtwMinimap.getInstance().getMinimap().getHeight();


        double texturePosX = player.posX - minimap.getCenterX();
        double texturePosY = player.posZ - minimap.getCenterZ();

        double centerX = 0.5d + (texturePosX / textureWidth);
        double centerZ = 0.5d + (texturePosY / textureHeight);

        double width = mapSize * minimap.getZoom() / textureWidth;
        double height = mapSize * minimap.getZoom() / textureHeight;

        double uMin = centerX - width / 2d;
        double uMax = centerX + width / 2d;
        double vMin = centerZ - height / 2d;
        double vMax = centerZ + height / 2d;

        double uMinOver = 0;
        double uMaxOver = 0;
        double vMinOver = 0;
        double vMaxOver = 0;

        if (uMin < 0) {
            uMinOver = -uMin;
            uMin = 0;
        }
        if (uMax > 1) {
            uMaxOver = uMax - 1;
            uMax = 1;
        }
        if (vMin < 0) {
            vMinOver = -vMin;
            vMin = 0;
        }
        if (vMax > 1) {
            vMaxOver = vMax - 1;
            vMax = 1;
        }

        double xUnder = (uMinOver * textureWidth) / minimap.getZoom();
        double yUnder = (vMinOver * textureHeight) / minimap.getZoom();
        double xOver = (uMaxOver * textureWidth) / minimap.getZoom();
        double yOver = (vMaxOver * textureHeight) / minimap.getZoom();

        GlStateManager.pushMatrix();
        if (GtwMinimap.getInstance().isRotating()) {
            float angle = 180 - Minecraft.getMinecraft().player.rotationYaw;
            GLUtils.rotateFixed(GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), angle);
        }

        GLUtils.enableCircleStencil(GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius());

        GlStateManager.color(0, 0, 0, 1);
        Gui.drawRect(0, 0, GtwMinimap.getInstance().getRadius() * 2, GtwMinimap.getInstance().getRadius() * 2, 0xFF000000);

        GlStateManager.color(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GtwMinimap.getInstance().getMinimap().getTexture());

        GlStateManager.enableTexture2D();
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        drawTexturedRect(xUnder, yUnder, mapSize - xUnder - xOver, mapSize - yUnder - yOver, uMin, vMin, uMax, vMax);

        GLUtils.disableStencil(); // Disable clipping
        GlStateManager.popMatrix();
    }

    private void drawPlayer() {
        GlStateManager.color(1, 0, 0, 1);

        GLUtils.drawCircle(GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), 2, true);
    }

    private void drawMarkers() {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

        EntityPlayer player = Minecraft.getMinecraft().player;
        double playerX = player.posX;
        double playerZ = player.posZ;

        double zoom = 1d / GtwMinimap.getInstance().getMinimap().getZoom();
        double radius = GtwMinimap.getInstance().getRadius() * 1.25 * GtwMinimap.getInstance().getMinimap().getZoom();

        List<Marker> markers = GtwMinimap
                .getInstance()
                .getClientMarkerManager()
                .getMarkers(playerX - radius, playerZ - radius, playerX + radius, playerZ + radius);

        float angle = 180 - player.rotationYaw;

        GLUtils.enableCircleStencil(GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius());

        for (Marker marker : markers) {
            GlStateManager.pushMatrix();

            double x = GtwMinimap.getInstance().getRadius() + ((marker.getPosX() - playerX) * zoom);
            double y = GtwMinimap.getInstance().getRadius() + ((marker.getPosZ() - playerZ) * zoom);

            boolean overlap = GtwMinimap.getInstance().getClientMarkerManager().hasMarkerOverlap(marker);
            if (overlap) GlStateManager.color(1f, 1f, 1f, 0.25f);

            ResourceLocation texture = marker.getType().getTexture();
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

            double uMin = marker.getType().getAtlas().getUMin();
            double uMax = marker.getType().getAtlas().getUMax();
            double vMin = marker.getType().getAtlas().getVMin();
            double vMax = marker.getType().getAtlas().getVMax();

            double width = marker.getType().getAtlas().getWidth();
            double height = marker.getType().getAtlas().getHeight();

            if (GtwMinimap.getInstance().isRotating()) {
                GLUtils.rotateFixed(GtwMinimap.getInstance().getRadius(), GtwMinimap.getInstance().getRadius(), angle);
                GLUtils.rotateFixed(x, y, 362 - angle);
            }

            x -= width / 2d;
            y -= height / 2d;

            drawTexturedRect(x, y, width, height, uMin, vMin, uMax, vMax);

            GlStateManager.popMatrix();
        }

        GLUtils.disableStencil(); // Disable clipping
        GlStateManager.popMatrix();
    }

    public static void drawTexturedRect(double x, double y, double w, double h, double u1, double v1, double u2, double v2) {
        GlStateManager.enableTexture2D();
//        GlStateManager.enableAlpha();
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
//        GlStateManager.disableAlpha();
    }


}
