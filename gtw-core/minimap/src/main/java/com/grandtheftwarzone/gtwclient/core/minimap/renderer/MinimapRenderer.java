package com.grandtheftwarzone.gtwclient.core.minimap.renderer;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import com.grandtheftwarzone.gtwclient.core.minimap.utils.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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
        List<Marker> markers = GTWMinimap.getInstance().getMarkerManager().queryMinimap(GTWMinimap.getInstance().getMinimap());

        if (GTWMinimap.getInstance().isRotating()) {
            float angle = 180 - Minecraft.getMinecraft().player.rotationYaw;
            GLUtils.rotateFixed(GTWMinimap.getInstance().getRadius(), GTWMinimap.getInstance().getRadius(), angle);
        }

        double playerX = Minecraft.getMinecraft().player.posX;
        double playerZ = Minecraft.getMinecraft().player.posZ;

        for (Marker marker : markers) {
            double x = (double) (marker.getPosX() - GTWMinimap.getInstance().getMinimap().getStartX()) / (GTWMinimap.getInstance().getMinimap().getEndX() - GTWMinimap.getInstance().getMinimap().getStartX()) * GTWMinimap.getInstance().getRadius() * 2;
            double y = (double) (marker.getPosZ() - GTWMinimap.getInstance().getMinimap().getStartZ()) / (GTWMinimap.getInstance().getMinimap().getEndZ() - GTWMinimap.getInstance().getMinimap().getStartZ()) * GTWMinimap.getInstance().getRadius() * 2;

            float size = GTWMinimap.getInstance().getMarkerSize();

            double distance = Math.sqrt(Math.pow(marker.getPosX() - playerX, 2) + Math.pow(marker.getPosZ() - playerZ, 2));
            if (distance > GTWMinimap.getInstance().getRadius() - size / 2)
                continue; // Check if marker is outside minimap (circle)

            GlStateManager.color(marker.getType().getRed(), marker.getType().getGreen(), marker.getType().getBlue(), 1);
            GLUtils.drawCircle(x, y, GTWMinimap.getInstance().getMarkerSize(), true);
        }
    }
}
