package me.phoenixra.gtwclient.api.gui.impl;

import lombok.Getter;
import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.api.gui.*;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.api.font.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public class GuiElementText extends BaseGuiElement {
    private CustomFontRenderer fontRenderer;

    private Supplier<String> text;
    private GuiElementColor color;
    public GuiElementText(GtwGuiMenu guiMenu,
                          GuiElementLayer layer,
                          CustomFontRenderer fontRenderer,
                          Supplier<String> text,
                          GuiElementColor color,
                          PositionFunction functionX,
                          PositionFunction functionY,
                          PositionFunction functionWidth,
                          PositionFunction functionHeight) {
        super(guiMenu, layer, functionX, functionY, functionWidth, functionHeight);
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.color = color;
    }

    @Override
    public void handleDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        fontRenderer.setScaleX(scaleX);
        fontRenderer.setScaleY(scaleY);
        fontRenderer.drawString(
                text.get(),
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                color.toInt(),
                false
        );
    }

    public static Builder builder(GtwGuiMenu guiMenu) {
        return new Builder(guiMenu);
    }
    public static class Builder implements GuiElementBuilder {
        @Getter
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
        public Builder setColor(String jsonPath) {
            this.color = GTWClient.settings.getColorValue(jsonPath);
            return this;
        }
        @Override
        public Builder setX(PositionFunction functionX) {
            this.functionX = functionX;
            return this;
        }
        @Override
        public Builder setY(PositionFunction functionY) {
            this.functionY = functionY;
            return this;
        }
        @Override
        public Builder setWidth(PositionFunction functionWidth) {
            this.functionWidth = functionWidth;
            return this;
        }
        @Override
        public Builder setHeight(PositionFunction functionHeight) {
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
