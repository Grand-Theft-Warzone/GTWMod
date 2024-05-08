package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public interface MapMarker {

    String getIdentificator();

    @Nullable
    String getName();

    @Nullable
    String getLore();

    ResourceLocation getIcon();

    EntityLocation getWorldLocation();

    MapLocation getMapLocation();

    List<String> getMapImageIds();

    boolean isLocalMarker();

    boolean isDraw();

    @Nullable
    List<String> getActionList();


    // ==============

    void setDraw(boolean var1);
}
