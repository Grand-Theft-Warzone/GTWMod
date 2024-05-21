package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.ConfigOwner;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumconfig.api.config.serialization.ConfigSerializer;
import me.phoenixra.atumconfig.core.config.AtumConfigSection;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
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

    @Getter @Setter
    @Nullable
    private Config data;


    public BaseStaticMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, String iconId, EntityLocation worldLocation, @Nullable Config data, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList, boolean draw) {
        this.identificator = indentificator;
        this.name = name;
        this.lore = lore;
        this.icon = icon;
        this.iconId = iconId;
        this.data = data;
        this.worldLocation = worldLocation;
        this.localMarker = localMarker;
        this.mapImageIds = mapImageIds;
        this.actionList = actionList;
        this.draw = draw;
    }

    public BaseStaticMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, String iconId, EntityLocation worldLocation, @Nullable Config data, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList) {
        this(indentificator, name, lore, icon, iconId, worldLocation, data, localMarker, mapImageIds, actionList, true);
    }

    public BaseStaticMarker(TemplateMarker templateMarker) {
        this.identificator = templateMarker.getIdentificator();
        this.name = templateMarker.getName();
        this.lore = templateMarker.getLore();
        this.icon = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage("markers/"+templateMarker.getIconId());
        this.iconId = templateMarker.getIconId();
        this.data = templateMarker.getData();
        this.worldLocation = new EntityLocation(templateMarker.getWorldLocation());
        this.localMarker = templateMarker.isLocalMarker();
        this.mapImageIds = templateMarker.getMapImageIds();
        this.actionList = templateMarker.getActionList();
        this.draw = templateMarker.isDraw();
    }


    public MapLocation getMapLocation(String typeMap) {

        MapImage mapImage = getMapImage(typeMap);

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

    @Override
    public void update(TemplateMarker templateMarker) {
        // ID is not updated.
        this.name = templateMarker.getName();
        this.lore = templateMarker.getLore();

        if (this.iconId != null && !this.iconId.equals(templateMarker.getIconId())) {
            this.icon = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getImage("markers/"+templateMarker.getIconId());
        }
        this.iconId = templateMarker.getIconId();
        this.data = templateMarker.getData();
        this.worldLocation = new EntityLocation(templateMarker.getWorldLocation());
        this.localMarker = templateMarker.isLocalMarker();
        this.mapImageIds = templateMarker.getMapImageIds();
        this.actionList = templateMarker.getActionList();
        this.draw = templateMarker.isDraw();
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

    @Override
    public @NotNull Config serializeToConfig(@NotNull ConfigOwner configOwner, @Nullable MapMarker mapMarker) {
        Config config = new AtumConfigSection(configOwner, ConfigType.JSON, null);
        Config subsec = new AtumConfigSection(configOwner, ConfigType.JSON, null);

        subsec.set("name", this.getName());
        subsec.set("lore", this.getLore());
        subsec.set("icon", this.getIconId());
        subsec.set("data", this.getData() != null ? this.getData().toPlaintext() : null);
        subsec.set("worldLocation", this.getWorldLocation().toString());
        subsec.set("localMarker", this.isLocalMarker());
        subsec.set("mapImageIds", this.getMapImageIds());
        subsec.set("actionList", this.getActionList());
        subsec.set("draw", this.isDraw());

        config.set(this.getIdentificator(), subsec);

        return config;
    }
}
