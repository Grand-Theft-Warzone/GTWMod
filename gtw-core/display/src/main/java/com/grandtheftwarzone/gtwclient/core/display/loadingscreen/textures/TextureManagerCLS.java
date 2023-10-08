package com.grandtheftwarzone.gtwclient.core.display.loadingscreen.textures;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class TextureManagerCLS extends TextureManager {

    private final Set<ResourceLocation> textures = new HashSet<>();

    public TextureManagerCLS(IResourceManager resourceManager) {
        super(resourceManager);
    }

    @Override
    public void bindTexture(ResourceLocation resource) {
        super.bindTexture(resource);
        textures.add(resource);
    }

    @Override
    public void deleteTexture(ResourceLocation textureLocation) {
        super.deleteTexture(textureLocation);
        textures.remove(textureLocation);
    }

    @Override
    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        textures.add(textureLocation);
        return super.loadTexture(textureLocation, textureObj);
    }

    @Override
    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        textures.add(textureLocation);
        return super.loadTickableTexture(textureLocation, textureObj);
    }

    public void deleteAll() {
        for (ResourceLocation location : textures.toArray(new ResourceLocation[0])) {
            deleteTexture(location);
        }
    }
}
