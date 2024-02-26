package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.misc.AtumColor;

@Getter
public class ColorFilter {

    private final AtumColor color;
    private final float opacity;

    public ColorFilter(AtumColor color, float opacity) {
        this.color = color;
        this.opacity = opacity;
    }

}
