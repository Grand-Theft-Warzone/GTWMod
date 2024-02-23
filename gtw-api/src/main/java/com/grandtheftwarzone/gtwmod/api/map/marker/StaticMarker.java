package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import net.minecraft.util.ResourceLocation;

public interface StaticMarker {

    ResourceLocation getIcon();

    MapLocation getMapLocation();

    EntityLocation getWorldLocation();
}
