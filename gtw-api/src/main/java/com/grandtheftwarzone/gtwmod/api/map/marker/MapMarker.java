package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import me.phoenixra.atumconfig.api.config.serialization.ConfigSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public interface MapMarker extends ConfigSerializer<MapMarker> {

    String getIdentificator();

    @Nullable
    String getName();

    @Nullable
    String getLore();

    ResourceLocation getIcon();

    @Nullable
    String getIconId();

    EntityLocation getWorldLocation();

    MapLocation getMapLocation();

    MapLocation getMapLocation(String typeMap);

    List<String> getMapImageIds();

    boolean isLocalMarker();

    boolean isDraw();

    @Nullable
    List<String> getActionList();

    void update(TemplateMarker var);


    // ==============

    void setDraw(boolean var1);
}
