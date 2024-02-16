package com.grandtheftwarzone.gtwmod.core.display.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.minimap.DynamicMarker;
import com.grandtheftwarzone.gtwmod.api.gui.minimap.DynamicMarkerImpl;
import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import com.grandtheftwarzone.gtwmod.api.gui.minimap.MapImage;
import lombok.Getter;
import lombok.NonNull;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RadarPlayer extends DynamicMarkerImpl {

    public RadarPlayer(EntityCord playerCord, MapImage mapImage, ResourceLocation icon, double coef, double step) {
        super(playerCord, mapImage, icon, coef, step);
    }

    public RadarPlayer(EntityCord playerCord, MapImage mapImage, double coef, double step) {
        super(playerCord, mapImage, null, coef, step);
    }


    public void updateColorFilter() {
        float proximity = getMapImage().proximityToTheBorder(getCordMarker().getX(), getCordMarker().getZ());
        float opacity = 0;
        if (proximity > 0.9) {
            opacity = (proximity - 0.9F) * 0.9F * 10;
        }

        GtwAPI.getInstance().getGtwMinimapManager().setColorFilter(new ColorFilter(AtumColor.BLACK, opacity));
    }

    public boolean inMap() {
        return this.getMapImage().inRealMap(getCordMarker().getX(), getCordMarker().getZ());
    }


}
