package me.phoenixra.gtwclient.api.gui.impl;

import lombok.Getter;
import me.phoenixra.gtwclient.api.gui.*;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;


public class GuiElementImage extends BaseGuiElement {

    private final Runnable imageBinder;
    private final GuiElementColor color;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;


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
        super(guiMenu, layer, functionX, functionY, functionWidth, functionHeight);
        this.imageBinder = imageBinder;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.color = color;
    }
    @Override
    public void handleDraw(int scaleFactor, float windowRationX, float windowRationY) {
        GL11.glPushMatrix();
        color.useColor();
        imageBinder.run();
        if(textureHeight==-1){
            RenderUtils.drawCompleteImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
        }else {
            RenderUtils.drawPartialImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    textureX,
                    textureY,
                    textureWidth,
                    textureHeight
            );
        }
        GL11.glPopMatrix();
    }
    public static Builder builder(GtwGuiMenu guiMenu) {
        return new Builder(guiMenu);
    }
    public static class Builder implements GuiElementBuilder {
        @Getter
        private GtwGuiMenu guiMenu;
        private GuiElementLayer layer = GuiElementLayer.MIDDLE;
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

        @Override
        public Builder setLayer(GuiElementLayer layer) {
            this.layer = layer;
            return this;
        }

        public Builder setColor(GuiElementColor color) {
            this.color = color;
            return this;
        }
        public Builder setTextureX(int textureX) {
            this.textureX = textureX;
            return this;
        }
        public Builder setTextureY(int textureY) {
            this.textureY = textureY;
            return this;
        }
        public Builder setTextureWidth(int textureWidth) {
            this.textureWidth = textureWidth;
            return this;
        }
        public Builder setTextureHeight(int textureHeight) {
            this.textureHeight = textureHeight;
            return this;
        }
        public Builder setImageBinder(Runnable imageBinder) {
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
