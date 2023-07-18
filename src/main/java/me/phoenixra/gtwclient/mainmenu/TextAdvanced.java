package me.phoenixra.gtwclient.mainmenu;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.utils.RenderUtils;
import me.phoenixra.gtwclient.api.font.GlyphPage;
import me.phoenixra.gtwclient.api.font.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.InputStream;
import java.util.function.Function;
import java.util.function.Supplier;

public class TextAdvanced {
    private Supplier<String> text;
    private int color;

    private Function<Float,Integer> functionX;
    private Function<Float,Integer> functionY;

    private Function<Integer,Float> functionScaleX;
    private Function<Integer,Float> functionScaleY;

    private CustomFontRenderer fontRenderer;
    private GlyphPage glyphPage;
    public TextAdvanced(Supplier<String> text,
                        int color,
                        Function<Float,Integer> functionX,
                        Function<Float,Integer> functionY,
                        Function<Integer,Float> functionScaleX,
                        Function<Integer,Float> functionScaleY) {
        this.text = text;
        this.color = color;
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionScaleX = functionScaleX;
        this.functionScaleY = functionScaleY;
        try(InputStream stream =
                    GTWClient.class.getResourceAsStream("/assets/gtwclient/fonts/main_menu.ttf")) {
            Font font = Font.createFont(
                    0,
                    stream
            );
            boolean b = GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            glyphPage = new GlyphPage(
                    new Font(font.getFontName(), Font.PLAIN, 80),
                    false,false
            );
            char[] chars = new char[256];
            for(int i = 0; i < 256; i++){
                chars[i] = (char)i;
            }
            glyphPage.generateGlyphPage(chars);
            fontRenderer = new CustomFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void drawText(Minecraft mc) {
        GL11.glPushMatrix();
        float windowRationX = (float) RenderUtils.getWindowRatioWidth();
        float windowRationY = (float) RenderUtils.getWindowRatioHeight();
        int scaleFactor = RenderUtils.getScaleFactor();
        float scaleX = functionScaleX.apply(scaleFactor) * windowRationX;
        float scaleY = functionScaleY.apply(scaleFactor) * windowRationY;

        GL11.glScalef(scaleX, scaleY, 1.0F);
        fontRenderer.drawString(
                text.get(),
                (int)(functionX.apply(scaleFactor * scaleX) * windowRationX),
                (int)(functionY.apply(scaleFactor * scaleY) * windowRationY),
                color,
                false
        );
        GL11.glPopMatrix();
        /*
        GL11.glScalef(scaleX, scaleY, 1.0F);
        this.drawCenteredString(
                fontRenderer,
                text.get(),
                (int)(functionX.apply(scaleFactor * scaleX) * windowRationX),
                (int)(functionY.apply(scaleFactor * scaleY) * windowRationY),
                color
        );

        */

    }


    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }
}
