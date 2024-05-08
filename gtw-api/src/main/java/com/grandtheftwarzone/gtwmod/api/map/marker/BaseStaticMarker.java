package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class BaseStaticMarker implements MapMarker {

    private String identificator;

    private ResourceLocation icon;

    private String name;

    private String lore;

    private List<String> mapImageIds;

    private boolean localMarker;

    @Setter
    private boolean draw;

    private List<String> actionList;

    private EntityLocation worldLocation;


    public BaseStaticMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, EntityLocation worldLocation, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList, boolean draw) {
        this.identificator = indentificator;
        this.name = name;
        this.lore = lore;
        this.icon = icon;
        this.worldLocation = worldLocation;
        this.localMarker = localMarker;
        this.mapImageIds = mapImageIds;
        this.actionList = actionList;
        this.draw = draw;
    }

    public BaseStaticMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, EntityLocation worldLocation, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList) {
        this(indentificator, name, lore, icon, worldLocation, localMarker, mapImageIds, actionList, true);
    }


    public MapLocation getMapLocation(String targetMap) {

        MapImage mapImage = getMapImage(targetMap);

        if (!this.mapImageIds.contains(mapImage.getImageId())) {
            GtwLog.getLogger().error("[getMapLocation] Error!");
            return new MapLocation(-999999999,-999999999,-404);
        }

        if (!draw) {
            GtwLog.getLogger().error("[getMapLocation] Error! draw disable.");
            return new MapLocation(-999999999,-999999999,-405);
        }

        return mapImage.calculateImageCoord(worldLocation.getX(), worldLocation.getY());
    }

    public MapLocation getMapLocation() {
        return this.getMapLocation("globalmap");
    }

    public MapImage getMapImage(String targetMap) {
        if (Objects.equals(targetMap, "minimap")) {
            return GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage();
        } else if (Objects.equals(targetMap, "globalmap")) {
            return GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage();
        }
        return null;
    }

}
