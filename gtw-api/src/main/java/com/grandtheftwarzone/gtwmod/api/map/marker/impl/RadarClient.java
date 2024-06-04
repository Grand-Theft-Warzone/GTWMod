package com.grandtheftwarzone.gtwmod.api.map.marker.impl;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseDynamicMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class RadarClient extends BaseDynamicMarker {

    public RadarClient(EntityLocation playerLocation, String identificator, String name, ResourceLocation icon, String iconId, double coef, double step) {
        super(identificator, name, null, icon, iconId, playerLocation, coef, step, null, true, null, null, true);
    }

    public RadarClient(EntityLocation playerLocation, String identificator, String name, double coef, double step) {
        super(identificator, name, null, null, null, playerLocation, coef, step, null, true, null, null, true);
    }

    public RadarClient(EntityLocation playerLocation, String identificator, String name, ResourceLocation icon, double coef, double step) {
        super(identificator, name, null, icon, null, playerLocation, coef, step,  null,true, null, null, true);
    }


//    GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getResourceLocation("radarImage")


    @Override
    public ResourceLocation getIcon() {
        try {
            return GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getResourceLocation("radarImage");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MapLocation getMapLocation(String targetMap) {
        getWorldLocation().update(Minecraft.getMinecraft().player);
        return super.getMapLocation(targetMap);
    }

    public void updateColorFilter() {
        float proximity = getMapImage("minimap").proximityToTheBorder(getWorldLocation().getX(), getWorldLocation().getZ());
        float opacity = 0;
        if (proximity > 0.9) {
            opacity = (proximity - 0.9F) * 0.9F * 10;
        }
        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setOpacityFilter(opacity);
    }

    public boolean inMap() {
        getWorldLocation().update(Minecraft.getMinecraft().player);
        return this.getMapImage("minimap").inRealMap(getWorldLocation().getX(), getWorldLocation().getZ());

    }


}
