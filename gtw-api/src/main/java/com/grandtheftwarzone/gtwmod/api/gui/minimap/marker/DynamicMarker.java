package com.grandtheftwarzone.gtwmod.api.gui.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface DynamicMarker {
    
    List<MapLocation> getMapInterpolations();
    ResourceLocation getIcon();

    EntityLocation getWorldLocation();

    MapLocation getCurrentMapLocation();
}
