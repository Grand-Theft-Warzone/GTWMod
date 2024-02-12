package com.grandtheftwarzone.gtwmod.api.gui.minimap;

import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;

public interface MinimapManager {

    void updateMinimapManager(DisplayRenderer renderer);

    /**
     * Get whether the minimap should be displayed.
     */
    boolean getIsActive();

    /**
     * Get the color worth displaying.
     * @return default frame color
     */
    AtumColor getColorFrame();

    /**
     * Set default border color
     * @param color - frame color
     */
    void setDefaultColorFrame(AtumColor color);

    /**
     * Set frame color for time
     * @param color - frame color
     * @param time - how many ticks to keep
     */
    void setColorFrame(AtumColor color, int time);


    ResourceLocation getResourceLocation(String imageName) throws NoSuchFieldException, IllegalAccessException;

    ColorFilter getColorFilter();

    void setColorFilter(ColorFilter filter);


}
