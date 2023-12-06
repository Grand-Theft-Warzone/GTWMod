package com.grandtheftwarzone.gtwclient.core.minimap.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class TextureAtlas {
    private final int index;
    private final int textureWidth;
    private final int textureHeight;
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;

    public TextureAtlas(int index, int textureWidth, int textureHeight, int width, int height) {
        this.index = index;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureX = (index % (textureWidth / width)) * width;
        this.textureY = (index / (textureHeight / height)) * height;
        this.width = width;
        this.height = height;
    }

    public double getUMin() {
        return (double) textureX / textureWidth;
    }

    public double getUMax() {
        return (double) (textureX + width) / textureWidth;
    }

    public double getVMin() {
        return (double) textureY / textureHeight;
    }

    public double getVMax() {
        return (double) (textureY + height) / textureHeight;
    }
}