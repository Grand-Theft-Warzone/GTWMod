package me.phoenixra.gtwclient.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.Supplier;

public class TextAdvanced {
    private Supplier<String> text;
    private int color;

    private int x;
    private int y;

    public TextAdvanced(Supplier<String> text,
                       int color,
                       int x,
                       int paddingX,
                       int y,
                       int paddingY) {
        this.text = text;
        this.color = color;
        this.x = x+paddingX;
        this.y = y+paddingY;
    }


    public void drawText(Minecraft mc) {
        FontRenderer fontrenderer = mc.fontRenderer;
        this.drawCenteredString(fontrenderer,
                text.get(),
                this.x,
                this.y,
                color
        );

    }


    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }
}
