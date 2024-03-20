package com.grandtheftwarzone.gtwmod.api.misc;

import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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

    // ZEVS

    public static void drawHollowCircle(float centerX, float centerY,
                                        float radius,
                                        float thickness,
                                        AtumColor color){

        GL11.glPushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        color.useColor();

        GL11.glLineWidth(thickness);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < 360; i++) {
            float radian = (float) Math.toRadians(i);
            float x = centerX + radius * (float) Math.cos(radian);
            float y = centerY + radius * (float) Math.sin(radian);
            GL11.glVertex2f(x, y);
        }
        GL11.glEnd();
        GlStateManager.resetColor();
        GlStateManager.color(1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GL11.glPopMatrix();

    }



    public static void drawPartialImage(int posX, int posY, int width, int height, int textureX, int textureY, int texturePartWidth, int texturePartHeight) {
        double imageWidth = (double) GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        double imageHeight = (double) GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        double einsTeilerWidth = 1.0 / imageWidth;
        double uvWidth = einsTeilerWidth * (double)texturePartWidth;
        double uvX = einsTeilerWidth * (double)textureX;
        double einsTeilerHeight = 1.0 / imageHeight;
        double uvHeight = einsTeilerHeight * (double)texturePartHeight;
        double uvY = einsTeilerHeight * (double)textureY;

        // Set the clamping mode for the texture
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslatef((float)posX, (float)posY, 0.0F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(uvX, uvY);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2d(uvX, uvY + uvHeight);
        GL11.glVertex3f(0.0F, (float)height, 0.0F);
        GL11.glTexCoord2d(uvX + uvWidth, uvY + uvHeight);
        GL11.glVertex3f((float)width, (float)height, 0.0F);
        GL11.glTexCoord2d(uvX + uvWidth, uvY);
        GL11.glVertex3f((float)width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glTranslatef((float)(-posX), (float)(-posY), 0.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void drawText(int x, int y, String text, AtumColor color) {

        float red = color.getRed() / 255.0f;
        float green = color.getGreen() / 255.0f;
        float blue = color.getBlue() / 255.0f;
        float alpha = color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawString(text, x, y, color.toInt(), true);

    }
}
