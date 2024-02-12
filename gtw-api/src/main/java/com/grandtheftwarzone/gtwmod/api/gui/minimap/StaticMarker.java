package com.grandtheftwarzone.gtwmod.api.gui.minimap;

import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

public interface StaticMarker {
    EntityCord cord = null;

    ResourceLocation icon = null;

    EntityCord getPlayerCord();

    ResourceLocation getIcon();

}
