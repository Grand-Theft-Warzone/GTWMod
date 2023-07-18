package me.phoenixra.gtwclient.api.gui.impl;

import lombok.Getter;
import me.phoenixra.gtwclient.api.gui.GuiElement;
import me.phoenixra.gtwclient.api.gui.GuiElementColor;
import me.phoenixra.gtwclient.api.gui.GtwGuiMenu;
import me.phoenixra.gtwclient.api.gui.GuiElementLayer;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.api.font.CustomFontRenderer;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public class GuiElementText implements GuiElement {
    @Getter
    private GtwGuiMenu guiMenu;
    @Getter
    private GuiElementLayer layer;
    private CustomFontRenderer fontRenderer;

    private Supplier<String> text;
    private GuiElementColor color;

    private PositionFunction functionX;
    private PositionFunction functionY;

    private PositionFunction functionWidth;
    private PositionFunction functionHeight;

    private int savedX;
    private int savedY;
    private int savedWidth;
    private int savedHeight;
    public GuiElementText(GtwGuiMenu guiMenu,
                          GuiElementLayer layer,
                          CustomFontRenderer fontRenderer,
                          Supplier<String> text,
                          GuiElementColor color,
                          PositionFunction functionX,
                          PositionFunction functionY,
                          PositionFunction functionWidth,
                          PositionFunction functionHeight) {
        this.guiMenu = guiMenu;
        this.layer = layer;
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.color = color;
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionWidth = functionWidth;
        this.functionHeight = functionHeight;
    }

    @Override
    public void draw(int scaleFactor, float windowRationX, float windowRationY) {
        GL11.glPushMatrix();
        float finalScaleFactorX = windowRationX / scaleFactor;
        float finalScaleFactorY = windowRationX / scaleFactor;
        savedX = functionX.getValue(finalScaleFactorX);
        savedY = functionY.getValue(finalScaleFactorY);
        savedWidth = functionWidth.getValue(finalScaleFactorX);
        savedHeight = functionHeight.getValue(finalScaleFactorY);
        fontRenderer.drawString(
                text.get(),
                savedX,
                savedY,
                savedWidth,
                savedHeight,
                color.toInt(),
                false
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
    public static class Builder {
        private final GtwGuiMenu guiMenu;
        private GuiElementLayer layer;
        private CustomFontRenderer fontRenderer;
        private Supplier<String> text;
        private GuiElementColor color;
        private PositionFunction functionX;
        private PositionFunction functionY;
        private PositionFunction functionWidth;
        private PositionFunction functionHeight;
        public Builder(GtwGuiMenu guiMenu) {
            this.guiMenu = guiMenu;
        }
        public Builder setLayer(GuiElementLayer layer) {
            this.layer = layer;
            return this;
        }
        public Builder setFontRenderer(CustomFontRenderer fontRenderer) {
            this.fontRenderer = fontRenderer;
            return this;
        }
        public Builder setText(Supplier<String> text) {
            this.text = text;
            return this;
        }
        public Builder setColor(GuiElementColor color) {
            this.color = color;
            return this;
        }
        public Builder setFunctionX(PositionFunction functionX) {
            this.functionX = functionX;
            return this;
        }
        public Builder setFunctionY(PositionFunction functionY) {
            this.functionY = functionY;
            return this;
        }
        public Builder setFunctionWidth(PositionFunction functionWidth) {
            this.functionWidth = functionWidth;
            return this;
        }
        public Builder setFunctionHeight(PositionFunction functionHeight) {
            this.functionHeight = functionHeight;
            return this;
        }
        public GuiElementText build() {
            return new GuiElementText(
                    guiMenu,
                    layer,
                    fontRenderer,
                    text,
                    color,
                    functionX,
                    functionY,
                    functionWidth,
                    functionHeight
            );
        }
    }
}
