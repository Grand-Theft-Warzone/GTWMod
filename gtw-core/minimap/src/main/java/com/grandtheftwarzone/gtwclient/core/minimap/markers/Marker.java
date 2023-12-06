package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.math.Vec2f;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Marker {
    private final int posX;
    private final int posZ;
    private final MarkerType type;

    public Vec2f getVec() {
        return new Vec2f(posX, posZ);
    }

}
