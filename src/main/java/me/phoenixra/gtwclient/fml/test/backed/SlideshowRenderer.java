package me.phoenixra.gtwclient.fml.test.backed;

import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.gtwclient.fml.test.textures.TextureLoader;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class SlideshowRenderer implements ElementRenderer{

    private final int frameRate;

    private final int posX, posY;
    protected final ResourceLocation[] res;

    private int frameCounter = 0;
    private int currentImage;
    private int oldImage;

    private boolean transit;

    private boolean postInit;
    public SlideshowRenderer(int posX,
                             int posY,
                             int frameRate,
                             ResourceLocation[] res
    ) {
        this.frameRate = frameRate;
        this.posX = posX;
        this.posY = posY;
        this.res = res;
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void render(LoadingScreenRenderer renderer) {
        bindTexture(renderer);
        RenderUtils.drawCompleteImage(
                posX, posY,
                Display.getWidth(), Display.getHeight()
        );
        GL11.glColor4f(1, 1, 1, 1);
    }

    public void bindTexture(LoadingScreenRenderer renderer) {
        frameCounter++;
        if(transit){
            if(frameCounter <= 20){
                float f = (float) 1/20;
                float decr = f*frameCounter;
                GL11.glColor4f(1-decr, 1-decr, 1-decr, 1);
                TextureLoader.bindTexture(renderer.textureManager, res[oldImage]);
                return;
            }else if(frameCounter <= 40){
                float f = (float) 1/20;
                float incr = f*(frameCounter-20);
                GL11.glColor4f(0+incr, 0+incr, 0+incr, 1);
            }else{
                transit = false;
                frameCounter = 0;
            }
            TextureLoader.bindTexture(renderer.textureManager, res[currentImage]);
            return;
        }
        if(frameCounter >= frameRate){
            oldImage = currentImage;
            transit = true;
            frameCounter = 0;
            if(currentImage < res.length - 1){
                currentImage++;
            }else{
                currentImage = 0;
            }
        }
        TextureLoader.bindTexture(renderer.textureManager, res[currentImage]);
    }
}
