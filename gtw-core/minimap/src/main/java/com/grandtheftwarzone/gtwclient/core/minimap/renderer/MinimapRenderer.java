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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;

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
        drawPlayer();
        drawMarkers();

        //Draw border
        GlStateManager.color(0.2f, 0.2f, 0.2f, 1f);
        GLUtils.drawCircle(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), 2);

        GlStateManager.popMatrix();
    }


    private void drawMap() {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

        if (GTWMinimap.getInstance().isRotating()) {
            float angle = 270 - Minecraft.getMinecraft().player.rotationYaw;
            GLUtils.rotateFixed(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), angle);
        }

//        GlStateManager.enableTexture2D();
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GlStateManager.bindTexture(GTWMinimap.getInstance().getMapTexture().getId());

        GLUtils.enableCircleStencil(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius());
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, GTWMinimap.getInstance().getRadius() * 2, GTWMinimap.getInstance().getRadius() * 2, GTWMinimap.getInstance().getRadius() * 2, GTWMinimap.getInstance().getRadius() * 2);
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


//        GLUtils.enableCircleStencil(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius());
        for (Marker marker : markers) {
            GlStateManager.pushMatrix();

            double x = GTWMinimap.getInstance().getRadius() + (marker.getPosX() - Minecraft.getMinecraft().player.posX);
            double y = GTWMinimap.getInstance().getRadius() + (marker.getPosZ() - Minecraft.getMinecraft().player.posZ);

            boolean overlap = GTWMinimap.getInstance().getClientMarkerManager().hasMarkerOverlap(marker);
            if (overlap) GlStateManager.color(1f, 1f, 1f, 0.25f);

            ResourceLocation texture = marker.getType().getTexture();
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            double uMin = marker.getType().getAtlas().getUMin();
            double uMax = marker.getType().getAtlas().getUMax();
            double vMin = marker.getType().getAtlas().getVMin();
            double vMax = marker.getType().getAtlas().getVMax();

            double z = 0;
            double width = marker.getType().getAtlas().getWidth();
            double height = marker.getType().getAtlas().getHeight();

            if (GTWMinimap.getInstance().isRotating())
                GLUtils.rotateFixed(x + width / 2, y + height / 2, -angle); // Rotate marker to always face the player

            buffer.pos(x, y + height, z).tex(uMin, vMax).endVertex();
            buffer.pos(x + width, y + height, z).tex(uMax, vMax).endVertex();
            buffer.pos(x + width, y, z).tex(uMax, vMin).endVertex();
            buffer.pos(x, y, z).tex(uMin, vMin).endVertex();

            tessellator.draw();

            if (overlap) GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.popMatrix();
        }
//        GLUtils.disableStencil(); // Disable clipping
        GlStateManager.popMatrix();
    }
}
