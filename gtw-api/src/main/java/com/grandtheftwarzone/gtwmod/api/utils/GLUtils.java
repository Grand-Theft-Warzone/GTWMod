package com.grandtheftwarzone.gtwmod.api.utils;

import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static me.phoenixra.atumodcore.api.utils.RenderUtils.drawRect;
import static org.lwjgl.opengl.GL11.*;

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

    public static void drawRoundedRect(int posX, int posY,
                                       int width, int height,
                                       int radius, // радиус закругления углов
                                       AtumColor color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        color.useColor();
        GlStateManager.disableDepth();
        buffer.begin(7, DefaultVertexFormats.POSITION);

        // Верхняя левая часть
        drawRoundedRectSegment(buffer, posX + radius, posY + radius, radius, 180, 270);
        // Верхняя правая часть
        drawRoundedRectSegment(buffer, posX + width - radius, posY + radius, radius, 270, 360);
        // Нижняя правая часть
        drawRoundedRectSegment(buffer, posX + width - radius, posY + height - radius, radius, 0, 90);
        // Нижняя левая часть
        drawRoundedRectSegment(buffer, posX + radius, posY + height - radius, radius, 90, 180);

        // Соединение частей прямыми линиями
        buffer.pos(posX + radius, posY, 0).endVertex(); // левая верхняя точка
        buffer.pos(posX + width - radius, posY, 0).endVertex(); // правая верхняя точка
        buffer.pos(posX + width, posY + radius, 0).endVertex(); // верхняя правая точка
        buffer.pos(posX + width, posY + height - radius, 0).endVertex(); // нижняя правая точка
        buffer.pos(posX + width - radius, posY + height, 0).endVertex(); // правая нижняя точка
        buffer.pos(posX + radius, posY + height, 0).endVertex(); // левая нижняя точка
        buffer.pos(posX, posY + height - radius, 0).endVertex(); // нижняя левая точка
        buffer.pos(posX, posY + radius, 0).endVertex(); // верхняя левая точка

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f);
    }

    private static void drawRoundedRectSegment(BufferBuilder buffer, double centerX, double centerY, double radius, int startAngle, int endAngle) {
        double step = Math.PI / 12; // шаг аппроксимации окружности
        for (double angle = Math.toRadians(startAngle); angle <= Math.toRadians(endAngle); angle += step) {
            double x = centerX + Math.cos(angle) * radius;
            double y = centerY + Math.sin(angle) * radius;
            buffer.pos(x, y, 0).endVertex();
        }
    }



    public static void drawPartialImage(int posX, int posY, int width, int height, double textureX, double textureY, int texturePartWidth, int texturePartHeight) {

        double imageWidth = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        double imageHeight = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);

        double einsTeilerWidth = 1F / imageWidth;
        double uvWidth = einsTeilerWidth * texturePartWidth;
        double uvX = einsTeilerWidth * textureX;

        double einsTeilerHeight = 1F / imageHeight;
        double uvHeight = einsTeilerHeight * texturePartHeight;
        double uvY = einsTeilerHeight * textureY;

        GlStateManager.enableBlend();

        // === new
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // ====
        GlStateManager.enableAlpha();
        glTranslatef(posX, posY, 0);
        glBegin(GL_QUADS);

        glTexCoord2d(uvX, uvY);
        glVertex3f(0, 0, 0);
        glTexCoord2d(uvX, uvY + uvHeight);
        glVertex3f(0, height, 0);
        glTexCoord2d(uvX + uvWidth, uvY + uvHeight);
        glVertex3f(width, height, 0);
        glTexCoord2d(uvX + uvWidth, uvY);
        glVertex3f(width, 0, 0);
        glEnd();
        glTranslatef(-posX, -posY, 0);
        GlStateManager.disableBlend();

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

    public static void drawText(int x, int y, String text, AtumColor color, int fontSize, boolean drawShadow) {

        float red = color.getRed();
        float green = color.getGreen();
        float blue = color.getBlue();
        float alpha = color.getAlpha();
        GlStateManager.color(red, green, blue, alpha);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        GlStateManager.pushMatrix();
        GlStateManager.scale(fontSize / 12.0f, fontSize / 12.0f, 1.0f);
        fontRenderer.drawString(text, (int)(x / (fontSize / 12.0f)), (int)(y / (fontSize / 12.0f)), color.toInt(), drawShadow);
        GlStateManager.popMatrix();

    }
    public static void drawText(int x, int y, String text, AtumColor color, int fontSize) {
        drawText(x, y, text, color, fontSize, true);
    }

    /**
     * Рисует пунктирную линию.
     *
     * @param startX Начальная X-координата.
     * @param startY Начальная Y-координата.
     * @param endX   Конечная X-координата.
     * @param endY   Конечная Y-координата.
     * @param color  Цвет в формате ARGB (например, 0xFFFF0000 для красного цвета).
     * @param lineWidth Толщина линии.
     * @param dashLength Длина одного штриха.
     * @param gapLength Длина промежутка между штрихами.
     */
    public static void drawDashedLine(float startX, float startY, float endX, float endY, int color, float lineWidth, float dashLength, float gapLength) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;

        float dx = endX - startX;
        float dy = endY - startY;
        float totalLength = (float) Math.sqrt(dx * dx + dy * dy);
        float dashCount = totalLength / (dashLength + gapLength);
        float dashDx = (dx / totalLength) * dashLength;
        float dashDy = (dy / totalLength) * dashLength;
        float gapDx = (dx / totalLength) * gapLength;
        float gapDy = (dy / totalLength) * gapLength;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(lineWidth);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < dashCount; i++) {
            float sx = startX + i * (dashDx + gapDx);
            float sy = startY + i * (dashDy + gapDy);
            float ex = sx + dashDx;
            float ey = sy + dashDy;

            buffer.pos(sx, sy, 0.0).color(r, g, b, a).endVertex();
            buffer.pos(ex, ey, 0.0).color(r, g, b, a).endVertex();
        }

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

}
