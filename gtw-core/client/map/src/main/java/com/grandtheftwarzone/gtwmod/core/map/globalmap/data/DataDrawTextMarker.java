package com.grandtheftwarzone.gtwmod.core.map.globalmap.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.phoenixra.atumodcore.api.misc.AtumColor;

@Data
@AllArgsConstructor
public class DataDrawTextMarker {
    int posX;
    int posY;
    int textWidth;
    int textHeight;
    String text;
    int sizeText;
    AtumColor color;
}
