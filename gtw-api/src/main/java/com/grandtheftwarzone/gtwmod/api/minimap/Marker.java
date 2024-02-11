package com.grandtheftwarzone.gtwmod.api.minimap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.math.Vec2f;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Marker {
    private final double posX;
    private final double posZ;
    private final MarkerType type;

    public Vec2f getVec() {
        return new Vec2f((float) posX, (float) posZ);
    }

}
