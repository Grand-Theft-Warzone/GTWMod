package me.phoenixra.gtwclient.api.gui.impl;

import lombok.Getter;
import me.phoenixra.gtwclient.api.gui.GuiElement;
import me.phoenixra.gtwclient.api.gui.GtwGuiMenu;
import me.phoenixra.gtwclient.api.gui.GuiElementLayer;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;


public class GuiElementButton extends GuiButton implements GuiElement {
    @Getter
    private GtwGuiMenu guiMenu;
    @Getter
    private GuiElementLayer layer;
    private PositionFunction functionX;
    private PositionFunction functionY;

    private PositionFunction functionWidth;
    private PositionFunction functionHeight;


    protected Runnable imageBinder;
    private long lastClick = 0;
    protected boolean pressed;

    private float savedScaleFactor;
    private float savedWindowRationX;
    private float savedWindowRationY;

    private final Runnable actionOnClick;
    public GuiElementButton(GtwGuiMenu guiMenu,
                            GuiElementLayer layer,
                            PositionFunction functionX,
                            PositionFunction functionY,
                            PositionFunction functionWidth,
                            PositionFunction functionHeight,
                            Runnable imageBinder,
                            Runnable actionOnClick) {
        super(
                (int)(Math.random()*100000),
                functionX.getValue(1f),
                functionY.getValue(1f),
                functionWidth.getValue(1f),
                functionHeight.getValue(1f),
                ""
        );
        this.guiMenu = guiMenu;
        this.imageBinder = imageBinder;
        this.actionOnClick = actionOnClick;
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionWidth = functionWidth;
        this.functionHeight = functionHeight;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        pressed = false;
        if(System.currentTimeMillis()-lastClick>500) {
            lastClick = System.currentTimeMillis();
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(this.visible && isMouseOver()) {
            pressed = true;
            actionOnClick.run();
            return true;
        }
        return false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            GL11.glPushMatrix();
            float scaleFactorX = savedScaleFactor/savedWindowRationX;
            float scaleFactorY = savedScaleFactor/savedWindowRationY;
            x = this.functionX.getValue(scaleFactorX);
            y = this.functionY.getValue(scaleFactorY);
            width = this.functionWidth.getValue(scaleFactorX);
            height = this.functionHeight.getValue(scaleFactorY);

            imageBinder.run();
            this.hovered = isHovered(mouseX, mouseY);
            //GL color
            if(pressed){
                GlStateManager.color(1.8F, 1.8F, 1.8F);
            }
            else if(hovered) {
                GlStateManager.color(0.9f, 0.9F, 0.9F);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F);
            }
            RenderUtils.drawCompleteImage(x, y, width, height);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void draw(int scaleFactor, float windowRationX, float windowRationY) {
        savedScaleFactor = scaleFactor;
        savedWindowRationX = windowRationX;
        savedWindowRationY = windowRationY;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public static Builder builder(GtwGuiMenu guiMenu) {
        return new Builder(guiMenu);
    }
    public static class Builder {
        private GtwGuiMenu guiMenu;
        private GuiElementLayer layer;
        private PositionFunction functionX;
        private PositionFunction functionY;
        private PositionFunction functionWidth;
        private PositionFunction functionHeight;
        private Runnable imageBinder;
        private Runnable actionOnClick;
        public Builder(GtwGuiMenu guiMenu) {
            this.guiMenu = guiMenu;
        }
        public Builder setLayer(GuiElementLayer layer) {
            this.layer = layer;
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
        public Builder setImageBinder(Runnable imageBinder) {
            this.imageBinder = imageBinder;
            return this;
        }
        public Builder setActionOnClick(Runnable actionOnClick) {
            this.actionOnClick = actionOnClick;
            return this;
        }
        public GuiElementButton build() {
            return new GuiElementButton(guiMenu, layer, functionX, functionY, functionWidth, functionHeight, imageBinder, actionOnClick);
        }
    }


}
