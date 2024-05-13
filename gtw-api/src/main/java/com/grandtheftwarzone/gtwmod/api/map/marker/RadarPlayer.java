package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseDynamicMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RadarPlayer extends BaseDynamicMarker {

    public RadarPlayer(EntityLocation playerLocation, String identificator,  String name, ResourceLocation icon, String iconId, double coef, double step) {
        super(identificator, name, null, icon, iconId, playerLocation, coef, step, null, true, null, null, true);
    }

    public RadarPlayer(EntityLocation playerLocation, String identificator,  String name, double coef, double step) {
        super(identificator, name, null, null, null, playerLocation, coef, step, null, true, null, null, true);
    }

    public RadarPlayer(EntityLocation playerLocation, String identificator,  String name, ResourceLocation icon, double coef, double step) {
        super(identificator, name, null, icon, null, playerLocation, coef, step,  null,true, null, null, true);
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
