package com.grandtheftwarzone.gtwclient.core.minimap.utils;

import com.grandtheftwarzone.gtwclient.core.minimap.GTWMinimap;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public class MapTexture {
    private final DynamicTexture texture;

    public MapTexture() {
        int radius = GTWMinimap.getInstance().getRadius();
        texture = new DynamicTexture(radius * 2, radius * 2);

        Arrays.fill(texture.getTextureData(), 0xFF0000FF);

        texture.updateDynamicTexture();
    }

    public int getId() {
        return texture.getGlTextureId();
    }

    public void setIndex(int index, int color) {
        texture.getTextureData()[index] = color;
    }

    public void update() {
        texture.updateDynamicTexture();
    }
}
