package com.grandtheftwarzone.gtwclient.core.display.loadingscreen.backed;

import com.grandtheftwarzone.gtwclient.core.display.loadingscreen.textures.TextureLoader;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class SlideshowRenderer implements ElementRenderer {

    private final int frameRate;

    private final int posX, posY;
    protected final ResourceLocation[] res;
    private final List<TextureLoader.PreScannedImageData> preScannedImageData = new ArrayList<>();

    private int frameCounter = 0;
    private int currentImage;
    private int oldImage;

    private boolean transit;

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
        for(ResourceLocation r : res){
            preScannedImageData.add(TextureLoader.preScan(r));
        }
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
                if(preScannedImageData.get(oldImage) != null){
                    preScannedImageData.get(oldImage).bind(renderer.textureManager);
                }else {
                    TextureLoader.bindTexture(renderer.textureManager, res[oldImage]);
                }
                return;
            }else if(frameCounter <= 40){
                float f = (float) 1/20;
                float incr = f*(frameCounter-20);
                GL11.glColor4f(0+incr, 0+incr, 0+incr, 1);
            }else{
                transit = false;
                frameCounter = 0;
            }
            if(preScannedImageData.get(currentImage) != null){
                preScannedImageData.get(currentImage).bind(renderer.textureManager);
            }else {
                TextureLoader.bindTexture(renderer.textureManager, res[currentImage]);
            }
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
            if(preScannedImageData.get(oldImage) != null){
                preScannedImageData.get(oldImage).bind(renderer.textureManager);
            }else {
                TextureLoader.bindTexture(renderer.textureManager, res[oldImage]);
            }
            return;
        }
        if(preScannedImageData.get(currentImage) != null){
            preScannedImageData.get(currentImage).bind(renderer.textureManager);
        }else {
            TextureLoader.bindTexture(renderer.textureManager, res[currentImage]);
        }
    }
}
