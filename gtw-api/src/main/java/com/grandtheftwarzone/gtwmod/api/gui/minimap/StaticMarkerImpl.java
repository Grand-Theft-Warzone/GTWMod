package com.grandtheftwarzone.gtwmod.api.gui.minimap;

import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class StaticMarkerImpl implements StaticMarker {

    private EntityCord cordMarker;

    private MapImage mapImage;

    private MapCord mapCord;

    private ResourceLocation icon;

    public StaticMarkerImpl(EntityCord cordMarker, MapImage mapImage, ResourceLocation icon) {
        this.cordMarker = cordMarker;
        this.mapImage = mapImage;
        this.icon = icon;
    }

    @Override
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Override
    public MapCord getMapCord() {
        return this.mapImage.calculateCoord(cordMarker.getX(), cordMarker.getY());
    }

    @Override
    public EntityCord getRealCord() {
        return this.cordMarker;
    }
}
