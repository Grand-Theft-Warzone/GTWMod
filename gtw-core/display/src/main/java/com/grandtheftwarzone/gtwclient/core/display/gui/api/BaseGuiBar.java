package com.grandtheftwarzone.gtwclient.core.display.gui.api;


import com.grandtheftwarzone.gtwclient.core.display.hud.HudElement;

import java.util.function.Supplier;

public class BaseGuiBar {
    private final BaseGUI gui;
    private Supplier<Double> value;
    private int color;
    private boolean outlined;

    private int x;
    private int y;
    private int width;
    private int height;

    public BaseGuiBar(BaseGUI gui,
                       Supplier<Double> value,
                       int color,
                       boolean outlined,
                       int x,
                       int y,
                      int width,
                      int height) {
        this.gui = gui;
        this.value = value;
        this.color = color;
        this.outlined = outlined;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public void drawBar(int guiX, int guiY) {
        HudElement.drawCustomBar(
                guiX+x,
                guiY+y,
                width,
                height,
                value.get(),
                color,
                color,
                outlined
        );

    }

}
