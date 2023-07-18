package me.phoenixra.gtwclient.api.gui.impl;

import lombok.Getter;
import me.phoenixra.gtwclient.api.gui.*;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;


public class GuiElementImage implements GuiElement {
    @Getter
    private final GtwGuiMenu guiMenu;
    @Getter
    private final GuiElementLayer layer;
    private final PositionFunction functionX;
    private final PositionFunction functionY;

    private final PositionFunction functionWidth;
    private final PositionFunction functionHeight;

    private final Runnable imageBinder;
    private final GuiElementColor color;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;


    private int savedX;
    private int savedY;
    private int savedWidth;
    private int savedHeight;
    public GuiElementImage(GtwGuiMenu guiMenu,
                           GuiElementLayer layer,
                           PositionFunction  functionX,
                           PositionFunction  functionY,
                           PositionFunction functionWidth,
                           PositionFunction functionHeight,
                           Runnable imageBinder,
                           GuiElementColor color,
                           int textureX,
                           int textureY,
                           int textureWidth,
                           int textureHeight) {
        this.guiMenu = guiMenu;
        this.layer = layer;
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionWidth = functionWidth;
        this.functionHeight = functionHeight;
        this.imageBinder = imageBinder;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.color = color;
    }
    @Override
    public void draw(int scaleFactor, float windowRationX, float windowRationY) {
        GL11.glPushMatrix();
        GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue());
        imageBinder.run();
        float finalScaleFactorX = windowRationX / scaleFactor;
        float finalScaleFactorY = windowRationX / scaleFactor;
        savedX = functionX.getValue(finalScaleFactorX);
        savedY = functionY.getValue(finalScaleFactorY);
        savedWidth = functionWidth.getValue(finalScaleFactorX);
        savedHeight = functionHeight.getValue(finalScaleFactorY);
        RenderUtils.drawPartialImage(
                savedX,
                savedY,
                savedWidth,
                savedHeight,
                textureX,
                textureY,
                textureWidth,
                textureHeight
        );
        GL11.glPopMatrix();
    }

    @Override
    public int getX() {
        return savedX;
    }

    @Override
    public int getY() {
        return savedY;
    }

    @Override
    public int getWidth() {
        return savedWidth;
    }

    @Override
    public int getHeight() {
        return savedHeight;
    }
    public static Builder builder(GtwGuiMenu guiMenu) {
        return new Builder(guiMenu);
    }
    public static class Builder implements GuiElementBuilder {
        private GtwGuiMenu guiMenu;
        private GuiElementLayer layer = GuiElementLayer.MEDIUM;
        private PositionFunction functionX;
        private PositionFunction functionY;
        private PositionFunction functionWidth;
        private PositionFunction functionHeight;
        private GuiElementColor color = GuiElementColor.WHITE;
        private int textureX;
        private int textureY;
        private int textureWidth;
        private int textureHeight;
        private Runnable imageBinder;
        public Builder(GtwGuiMenu guiMenu) {
            this.guiMenu = guiMenu;
        }
        @Override
        public GuiElementBuilder setX(PositionFunction functionX) {
            this.functionX = functionX;
            return this;
        }
        @Override
        public GuiElementBuilder setY(PositionFunction functionY) {
            this.functionY = functionY;
            return this;
        }
        @Override
        public GuiElementBuilder setWidth(PositionFunction functionWidth) {
            this.functionWidth = functionWidth;
            return this;
        }
        @Override
        public GuiElementBuilder setHeight(PositionFunction functionHeight) {
            this.functionHeight = functionHeight;
            return this;
        }

        @Override
        public GuiElementBuilder setLayer(GuiElementLayer layer) {
            this.layer = layer;
            return this;
        }

        public GuiElementBuilder setColor(GuiElementColor color) {
            this.color = color;
            return this;
        }
        public GuiElementBuilder setTextureX(int textureX) {
            this.textureX = textureX;
            return this;
        }
        public GuiElementBuilder setTextureY(int textureY) {
            this.textureY = textureY;
            return this;
        }
        public GuiElementBuilder setTextureWidth(int textureWidth) {
            this.textureWidth = textureWidth;
            return this;
        }
        public GuiElementBuilder setTextureHeight(int textureHeight) {
            this.textureHeight = textureHeight;
            return this;
        }
        public GuiElementBuilder setImageBinder(Runnable imageBinder) {
            this.imageBinder = imageBinder;
            return this;
        }
        @Override
        public GuiElement build() {
            return new GuiElementImage(
                    guiMenu,
                    layer,
                    functionX,
                    functionY,
                    functionWidth,
                    functionHeight,
                    imageBinder,
                    color,
                    textureX,
                    textureY,
                    textureWidth,
                    textureHeight
            );
        }
    }

}
