package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;

public interface MinimapManager {

    void updateMinimapManager(DisplayRenderer renderer);

    /**
     * Get whether the minimap should be displayed.
     */
    boolean isActive();

    /**
     * Get the color that should be displayed.
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
     * @param time - the time period the color will be active
     */
    void setColorFrame(AtumColor color, int time);


    ResourceLocation getResourceLocation(String imageName) throws NoSuchFieldException, IllegalAccessException;

    ColorFilter getColorFilter();

    void setColorFilter(ColorFilter filter);

    MapImage getMinimapImage();

    // @TODO: remove
    boolean isAllowedToDisplay();

    void updateData(MapImage minimapData, ResourceLocation radarImage, boolean draw);

    void updateData(MapImage minimapData, ResourceLocation radarImage);

    int getMinZoom();
    int getMaxZoom();

    void updateZoomLimits(int min, int max);

    void setAllowedToDisplay(Boolean draw, Boolean quietChange);

    boolean isInitElementDraw();
    void setInitElementDraw(boolean draw);


}
