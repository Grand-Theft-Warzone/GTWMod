package me.phoenixra.gtwclient.mainmenu;

import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;
import java.util.function.Supplier;

public class TextAdvanced {
    private FontRenderer fontRenderer;
    private Supplier<String> text;
    private int color;

    private Function<Float,Integer> functionX;
    private Function<Float,Integer> functionY;

    private Function<Integer,Float> functionScaleX;
    private Function<Integer,Float> functionScaleY;
    public TextAdvanced(FontRenderer fontRenderer,
                        Supplier<String> text,
                        int color,
                        Function<Float,Integer> functionX,
                        Function<Float,Integer> functionY,
                        Function<Integer,Float> functionScaleX,
                        Function<Integer,Float> functionScaleY) {
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.color = color;
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionScaleX = functionScaleX;
        this.functionScaleY = functionScaleY;
    }


    public void drawText(Minecraft mc) {
        int scaleFactor = RenderUtils.getScaleFactor();
        float scaleX = functionScaleX.apply(scaleFactor);
        float scaleY = functionScaleY.apply(scaleFactor);
        GL11.glPushMatrix();
        GL11.glScalef(scaleX, scaleY, 1.0F);
        this.drawCenteredString(
                fontRenderer,
                text.get(),
                functionX.apply(scaleFactor * scaleX),
                functionY.apply(scaleFactor * scaleY),
                color
        );
        GL11.glPopMatrix();

    }


    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }
}
