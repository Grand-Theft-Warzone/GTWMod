package com.grandtheftwarzone.gtwmod.api.map.marker.impl;

import com.grandtheftwarzone.gtwmod.api.map.marker.BaseDynamicMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.FileImageUtils;
import me.phoenixra.atumconfig.api.config.Config;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerMarker extends BaseDynamicMarker {


    public PlayerMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, @Nullable String iconId, EntityLocation worldLocation, double coef, double step, @Nullable Config data, boolean localMarker, @Nullable List<String> mapImageIds, @Nullable List<String> actionList, boolean draw) {
        super(indentificator, name, lore, icon, iconId, worldLocation, coef, step, data, localMarker, mapImageIds, actionList, draw);
    }

    public PlayerMarker(TemplateMarker templateMarker) {
        super(templateMarker);
    }

    @Override
    public ResourceLocation getIcon() {
        return FileImageUtils.getPlayerHead(getName());
    }

}
