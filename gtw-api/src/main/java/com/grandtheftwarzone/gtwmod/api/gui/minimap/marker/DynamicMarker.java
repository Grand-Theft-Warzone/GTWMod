package com.grandtheftwarzone.gtwmod.api.gui.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface DynamicMarker {
    
    List<MapCord> getMapCords();
    ResourceLocation getIcon();

    EntityCord getRealCord();

    MapCord getDynamicMapCord();
}
