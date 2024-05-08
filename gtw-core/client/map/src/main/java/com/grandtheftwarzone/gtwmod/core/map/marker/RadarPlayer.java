package com.grandtheftwarzone.gtwmod.core.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;

import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseDynamicMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import net.minecraft.util.ResourceLocation;

public class RadarPlayer extends BaseDynamicMarker {

    public RadarPlayer(EntityLocation playerLocation, MapImage mapImage, ResourceLocation icon, double coef, double step) {
        super(playerLocation, mapImage, icon, coef, step);
    }

    public RadarPlayer(EntityLocation playerLocation, MapImage mapImage, double coef, double step) {
        super(playerLocation, mapImage, null, coef, step);
    }


    public void updateColorFilter() {
        float proximity = getMapImage().proximityToTheBorder(getWorldLocation().getX(), getWorldLocation().getZ());
        float opacity = 0;
        if (proximity > 0.9) {
            opacity = (proximity - 0.9F) * 0.9F * 10;
        }
        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setOpacityFilter(opacity);
    }

    public boolean inMap() {
        return this.getMapImage().inRealMap(getWorldLocation().getX(), getWorldLocation().getZ());
    }



}
