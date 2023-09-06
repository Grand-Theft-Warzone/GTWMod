package me.phoenixra.gtwclient.fml.test.backed;

import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.gtwclient.fml.test.textures.TextureLoader;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class CircleRenderer  implements ElementRenderer{


    private final int posX, posY,width,height;
    protected final ResourceLocation[] res;
    private final List<TextureLoader.PreScannedImageData> preScannedImageData = new ArrayList<>();
    private int currentImage;
    public CircleRenderer(int posX,
                             int posY,
                             int width,
                                int height,
                             ResourceLocation[] res
    ) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
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
                Display.getWidth() -  posX,Display.getHeight() - posY,
                width, height
        );
        GL11.glColor4f(1, 1, 1, 1);
    }


    public void bindTexture(LoadingScreenRenderer renderer) {
        if(currentImage < res.length - 1){
            currentImage++;
        }else{
            currentImage = 0;
        }
        if(preScannedImageData.get(currentImage) != null){
            preScannedImageData.get(currentImage).bind(renderer.textureManager);
        }else {
            TextureLoader.bindTexture(renderer.textureManager, res[currentImage]);
        }
    }
}
