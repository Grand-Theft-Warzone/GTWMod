package com.grandtheftwarzone.gtwclient.core.minimap.utils;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    public static void preDrawConstants() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
    }

    public static void postDrawConstants() {
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void setHexColor(int color) {
        float r = (float) ((color >> 16) & 0xFF) / 255.0F;
        float g = (float) ((color >> 8) & 0xFF) / 255.0F;
        float b = (float) ((color) & 0xFF) / 255.0F;
        float a = (float) ((color >> 24) & 0xFF) / 255.0F;

        GlStateManager.color(r, g, b, a);
    }

    public static void drawCircle(double x, double y, double radius, int width) {
        preDrawConstants();

        GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);

        double doublePi = Math.PI * 2;
        double angleChange = doublePi / 180;
        double r2 = radius + width;

        for (double angle = -angleChange; angle < doublePi; angle += angleChange) {
            double x2 = x + (Math.cos(-angle) * radius);
            double y2 = y + (Math.sin(-angle) * radius);

            double x3 = x + (Math.cos(-angle) * r2);
            double y3 = y + (Math.sin(-angle) * r2);

            //TODO: check if this is correct
            GlStateManager.glVertex3f((float) x2, (float) y2, 1);
            GlStateManager.glVertex3f((float) x3, (float) y3, 1);
        }

        GlStateManager.glEnd();

        postDrawConstants();
    }

    public static void drawCircle(double x, double y, double radius, boolean filled) {
        preDrawConstants();

        GlStateManager.glBegin(filled ? GL11.GL_POLYGON : GL11.GL_TRIANGLE_FAN);
        GlStateManager.glVertex3f((float) x, (float) y, 1);

        double doublePi = Math.PI * 2;
        double angleChange = doublePi / 180;

        for (double angle = -angleChange; angle < doublePi; angle += angleChange) {
            double x2 = x + (Math.cos(-angle) * radius);
            double y2 = y + (Math.sin(-angle) * radius);

            GlStateManager.glVertex3f((float) x2, (float) y2, 1);
        }

        GlStateManager.glEnd();

        postDrawConstants();
    }

    public static void drawCircle(double x, double y, double r) {
        preDrawConstants();

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex3d(x, y, 1);

        double doublePi = Math.PI * 2.0;
        double angleChange = doublePi / 180.0;

        for (double angle = -angleChange; angle < doublePi; angle += angleChange) {
            GL11.glVertex3d(x + (r * Math.cos(-angle)), y + (r * Math.sin(-angle)), 1);
        }

        GL11.glEnd();

        postDrawConstants();
    }

    public static void enableCircleStencil(double x, double y, double radius) {
        GlStateManager.enableDepth();
        GlStateManager.colorMask(false, false, false, false);
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(GL11.GL_ALWAYS);


        setHexColor(0xFFFFFFFF);
        drawCircle(x, y, radius);

        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(GL11.GL_GREATER);
    }

    public static void disableStencil() {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
    }

    public static void rotateFixed(double x, double y, float angle) {
        GlStateManager.translate(x, y, 0.0F);
        GlStateManager.rotate(angle, 0, 0, 1.0F);
        GlStateManager.translate(-x, -y, 0.0F);
    }

}
