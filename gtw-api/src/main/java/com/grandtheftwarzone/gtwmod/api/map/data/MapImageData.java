package com.grandtheftwarzone.gtwmod.api.map.data;

import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.phoenixra.atumodcore.api.misc.AtumColor;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MapImageData {

    private String imageId;
    private MapLocation topRight;
    private MapLocation downRight;
    private MapLocation downLeft;
    private MapLocation topLeft;
    @Nullable private AtumColor colorBackground;
    @Nullable private AtumColor colorBorderReach;

}
