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

import java.util.List;
import java.util.Objects;

@Getter
public class BaseStaticMarker implements MapMarker {

    @Getter
    private String identificator;

    @Nullable
    @Getter
    private ResourceLocation icon;

    @Nullable
    @Getter
    private String iconId;

    @Getter
    private String name;

    @Getter
    private String lore;

    @Nullable
    @Getter
    private List<String> mapImageIds;

    @Getter
    private boolean localMarker;

    @Getter @Setter
    private boolean draw;

    @Nullable
    @Getter
    private List<String> actionList;

    @Getter @Setter
    private EntityLocation worldLocation;


    public BaseStaticMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, String iconId, EntityLocation worldLocation, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList, boolean draw) {
        this.identificator = indentificator;
        this.name = name;
        this.lore = lore;
        this.icon = icon;
        this.iconId = iconId;
        this.worldLocation = worldLocation;
        this.localMarker = localMarker;
        this.mapImageIds = mapImageIds;
        this.actionList = actionList;
        this.draw = draw;
    }

    public BaseStaticMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, String iconId, EntityLocation worldLocation, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList) {
        this(indentificator, name, lore, icon, iconId, worldLocation, localMarker, mapImageIds, actionList, true);
    }

    public BaseStaticMarker(TemplateMarker templateMarker) {
        this.identificator = templateMarker.getIdentificator();
        this.name = templateMarker.getName();
        this.lore = templateMarker.getLore();
        this.icon = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage("marker/"+templateMarker.getIconId());
        this.iconId = templateMarker.getIconId();
        this.worldLocation = new EntityLocation(templateMarker.getWorldLocation());
        this.localMarker = templateMarker.isLocalMarker();
        this.mapImageIds = templateMarker.getMapImageIds();
        this.actionList = templateMarker.getActionList();
        this.draw = templateMarker.isDraw();
    }


    public MapLocation getMapLocation(String targetMap) {

        MapImage mapImage = getMapImage(targetMap);

        if (!this.mapImageIds.isEmpty() && !this.mapImageIds.contains(mapImage.getImageId())) {
            GtwLog.getLogger().error("[getMapLocation] Error! Displaying marker " + getIdentificator() + " is not allowed on canvas " + mapImage.getImageId());
            return new MapLocation(-999999999,-999999999,-404);
        }

        if (!draw) {
            GtwLog.getLogger().error("[getMapLocation] Error! Display of token " + getIdentificator() + " is disabled.");
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

    public MapImage getMapImage() {
        return this.getMapImage("globalmap");
    }

}
