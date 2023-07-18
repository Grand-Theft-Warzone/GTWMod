package me.phoenixra.gtwclient.mainmenu;

import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

public class ImageAdvanced {
    private Function<Float,Integer> functionX;
    private Function<Float,Integer> functionY;

    private Function<Integer,Float> functionWidth;
    private Function<Integer,Float> functionHeight;

    private float[] color;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;
    private ResourceLocation IMAGE;
    private Runnable imageBinder;
    public ImageAdvanced(Function<Float,Integer>  functionX,
                         Function<Float,Integer>  functionY,
                         Function<Integer,Float> functionWidth,
                         Function<Integer,Float> functionHeight,
                         Runnable imageBinder,
                         float[] color,
                         int textureX,
                         int textureY,
                         int textureWidth,
                         int textureHeight) {
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionWidth = functionWidth;
        this.functionHeight = functionHeight;
        this.imageBinder = imageBinder;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.color = color;
        /*
        DynamicTexture texture = new DynamicTexture(256, 256);
        GlStateManager.bindTexture((texture.getGlTextureId());*/
    }
    public ImageAdvanced(Function<Float,Integer>  functionX,
                         Function<Float,Integer>  functionY,
                         Function<Integer,Float> functionWidth,
                         Function<Integer,Float> functionHeight,
                         Runnable imageBinder,
                         int textureX,
                         int textureY,
                         int textureWidth,
                         int textureHeight) {
        this(
                functionX,
                functionY,
                functionWidth,
                functionHeight,
                imageBinder,
                new float[]{1.0f,1.0f,1.0f},
                textureX,
                textureY,
                textureWidth,
                textureHeight
        );
    }

    public void drawImage(Gui gui) {
        GL11.glPushMatrix();
        GlStateManager.color(color[0], color[1], color[2]);
        if(IMAGE==null) {
            imageBinder.run();
        }else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(IMAGE);
        }
        float windowRationX = (float) RenderUtils.getWindowRatioWidth();
        float windowRationY = (float) RenderUtils.getWindowRatioHeight();
        int scaleFactor = RenderUtils.getScaleFactor();
        float scaleX = functionWidth.apply(scaleFactor) * windowRationX;
        float scaleY = functionHeight.apply(scaleFactor) * windowRationY;
        GL11.glScalef(scaleX, scaleY, 1.0F);
        gui.drawTexturedModalRect(
                functionX.apply(scaleFactor * scaleX) * windowRationX,
                functionY.apply(scaleFactor * scaleY) * windowRationY,
                textureX,
                textureY,
                textureWidth,
                textureHeight
        );
        GL11.glPopMatrix();

    }
}
