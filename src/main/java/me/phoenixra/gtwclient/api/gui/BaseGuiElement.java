package me.phoenixra.gtwclient.api.gui;

import lombok.Getter;
import me.phoenixra.atumodcore.api.tuples.Pair;
import me.phoenixra.gtwclient.api.font.CustomFontRenderer;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class BaseGuiElement implements GuiElement {
    @Getter
    private GtwGuiMenu guiMenu;
    @Getter
    private GuiElementLayer layer;
    private PositionFunction functionX;
    private PositionFunction functionY;
    private PositionFunction functionWidth;
    private PositionFunction functionHeight;

    private Pair<Float, Integer> savedX;
    private Pair<Float, Integer> savedY;
    private Pair<Float, Integer> savedWidth;
    private Pair<Float, Integer> savedHeight;

    public BaseGuiElement(GtwGuiMenu guiMenu,
                          GuiElementLayer layer,
                          PositionFunction functionX,
                          PositionFunction functionY,
                          PositionFunction functionWidth,
                          PositionFunction functionHeight) {
        this.guiMenu = guiMenu;
        this.layer = layer;
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionWidth = functionWidth;
        this.functionHeight = functionHeight;

    }

    @Override
    public final void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        updateX(scaleX);
        updateY(scaleY);
        GL11.glPushMatrix();
        handleDraw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        GL11.glPopMatrix();
    }

    protected abstract void handleDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY);
    private void updateX(float scaleFactor){
        if(savedX == null || savedX.getFirst() != scaleFactor){
            savedX = new Pair<>(scaleFactor, functionX.getValue(scaleFactor));
        }
        if(savedWidth == null || savedWidth.getFirst() != scaleFactor){
            savedWidth = new Pair<>(scaleFactor, functionWidth.getValue(scaleFactor));
        }
    }
    private void updateY(float scaleFactor){
        if(savedY == null || savedY.getFirst() != scaleFactor){
            savedY = new Pair<>(scaleFactor, functionY.getValue(scaleFactor));
        }
        if(savedHeight == null || savedHeight.getFirst() != scaleFactor){
            savedHeight = new Pair<>(scaleFactor, functionHeight.getValue(scaleFactor));
        }
    }
    @Override
    public int getX() {
        return savedX.getSecond();
    }

    @Override
    public int getY() {
        return savedY.getSecond();
    }

    @Override
    public int getWidth() {
        return savedWidth.getSecond();
    }

    @Override
    public int getHeight() {
        return savedHeight.getSecond();
    }
}
