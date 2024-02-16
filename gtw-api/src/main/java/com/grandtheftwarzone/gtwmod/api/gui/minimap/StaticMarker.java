package com.grandtheftwarzone.gtwmod.api.gui.minimap;

import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import net.minecraft.util.ResourceLocation;

public interface StaticMarker {

    ResourceLocation getIcon();

    MapCord getMapCord();

    EntityCord getRealCord();
}
