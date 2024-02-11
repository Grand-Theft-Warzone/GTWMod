package com.grandtheftwarzone.gtwmod.core.display.gui.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.Supplier;

public class BaseGuiText {
    private final BaseGUI gui;

    private Supplier<String> text;
    private int color;

    private int x;
    private int y;

    public BaseGuiText(BaseGUI gui,
                         Supplier<String> text,
                         int color,
                         int x,
                         int y) {
        this.gui = gui;
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
    }


    public void drawText(Minecraft mc, int guiX, int guiY) {
        FontRenderer fontrenderer = mc.fontRenderer;
        this.drawCenteredString(fontrenderer,
                text.get(),
                guiX+this.x,
                guiY+this.y,
                color
        );

    }


    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }
}
