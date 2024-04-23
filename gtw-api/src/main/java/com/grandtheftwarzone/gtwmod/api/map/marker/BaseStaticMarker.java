package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import net.minecraft.util.ResourceLocation;

public class BaseStaticMarker implements StaticMarker {

    private EntityLocation worldLocationMarker;

    private MapImage mapImage;

    private MapLocation mapLocation;

    private ResourceLocation icon;

    public BaseStaticMarker(EntityLocation worldLocationMarker, MapImage mapImage, ResourceLocation icon) {
        this.worldLocationMarker = worldLocationMarker;
        this.mapImage = mapImage;
        this.icon = icon;
    }

    @Override
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Override
    public MapLocation getMapLocation() {
        return this.mapImage.calculateImageCoord(worldLocationMarker.getX(), worldLocationMarker.getY());
    }

    @Override
    public EntityLocation getWorldLocation() {
        return this.worldLocationMarker;
    }
}
