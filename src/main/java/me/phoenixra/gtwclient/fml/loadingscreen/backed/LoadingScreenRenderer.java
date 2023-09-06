package me.phoenixra.gtwclient.fml.loadingscreen.backed;

import lombok.Getter;
import me.phoenixra.gtwclient.fml.loadingscreen.textures.TextureManagerCLS;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class LoadingScreenRenderer {

    private long lastTime;
    private Minecraft mc;
    @Getter
    public TextureManagerCLS textureManager;
    private boolean first = true;


    private List<ElementRenderer> elements = new ArrayList<>();

    boolean init = false;
    public LoadingScreenRenderer() {
        mc = Minecraft.getMinecraft();

        textureManager = new TextureManagerCLS(mc.getResourceManager());
        mc.refreshResources();
        textureManager.onResourceManagerReload(mc.getResourceManager());

        lastTime = System.currentTimeMillis();

        elements.add(new SlideshowRenderer(
                0, 0, 100,
                new ResourceLocation[]{
                        new ResourceLocation("config:textures/loadingscreen/1.jpg"),
                        new ResourceLocation("config:textures/loadingscreen/2.jpg"),
                        new ResourceLocation("config:textures/loadingscreen/3.jpg"),
                        new ResourceLocation("config:textures/loadingscreen/4.jpg"),
                        new ResourceLocation("config:textures/loadingscreen/5.jpg")
                }
        ));
        elements.add(new CircleRenderer(
                60, 60, 40, 40,
                new ResourceLocation[]{
                        new ResourceLocation("config:textures/loadingscreen/circle/1.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/2.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/3.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/4.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/5.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/6.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/7.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/8.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/9.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/10.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/11.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/12.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/13.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/14.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/15.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/16.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/17.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/18.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/19.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/20.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/21.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/22.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/23.png"),
                        new ResourceLocation("config:textures/loadingscreen/circle/24.png"),
                }
        ));
    }

    public void render(){
        if(!init){
            init = true;
            elements.forEach(ElementRenderer::preLoad);
        }
        elements.forEach(element -> element.render(this));
    }
}
