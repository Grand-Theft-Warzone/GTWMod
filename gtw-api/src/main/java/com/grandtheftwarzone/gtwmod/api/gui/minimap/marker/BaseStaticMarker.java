package com.grandtheftwarzone.gtwmod.api.gui.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.gui.minimap.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import net.minecraft.util.ResourceLocation;

public class BaseStaticMarker implements StaticMarker {

    private EntityCord cordMarker;

    private MapImage mapImage;

    private MapCord mapCord;

    private ResourceLocation icon;

    public BaseStaticMarker(EntityCord cordMarker, MapImage mapImage, ResourceLocation icon) {
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
