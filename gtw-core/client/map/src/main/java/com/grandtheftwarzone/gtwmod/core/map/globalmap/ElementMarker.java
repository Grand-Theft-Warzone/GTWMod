package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseStaticMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.PlayerMarker;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVarInt;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementMarker extends BaseElement {

    MapMarker marker;

    public ElementMarker(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner, MapMarker marker) {
        super(atumMod, elementOwner);
        this.marker = marker;

    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

        if (marker instanceof PlayerMarker) {
            System.out.println("P");
            GlStateManager.pushMatrix();
            RenderUtils.bindTexture(marker.getIcon());
            GlStateManager.translate(getX(), getY(), 0);
            int iconRez = (int) (-getWidth() / 2);
            int borderThickness = 1;
            AtumColor color = AtumColor.GRAY;
            String gangMarkerStr = ((PlayerMarker) marker).getData().getSubsection("data").getStringOrNull("gang_id");
            String playerGangId = (GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getPlayerMarker() != null) ? GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getPlayerMarker().getData().getSubsection("data").getStringOrNull("gang_id") : null;
            if (gangMarkerStr != null && playerGangId != null && gangMarkerStr.equals(playerGangId)) {
                color = AtumColor.LIME;
            }
            RenderUtils.drawRect(iconRez - borderThickness, iconRez - borderThickness, iconRez + borderThickness*2, iconRez + borderThickness*2, color);


            Gui.drawModalRectWithCustomSizedTexture(
                    (int) (-getWidth() / 2),
                    (int) (-getWidth() / 2),
                    iconRez, iconRez,
                    iconRez, iconRez,
                    64, 64
            );
            GlStateManager.popMatrix();

        } else {

            RenderUtils.bindTexture(marker.getIcon());
            RenderUtils.drawCompleteImage(getX(), getY(), getWidth(), getWidth());

        }
    }


    public void setXY(int x, int y) {
        getOriginX().setDefaultValue(x);
        getOriginY().setDefaultValue(y);
    }

    public void setSize(int size) {
        getOriginWidth().setDefaultValue(size);
        getOriginHeight().setDefaultValue(size);
    }

    public void setXY(MapLocation location) {
        this.setXY((int) location.getX(), (int) location.getY());
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
