package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import lombok.Data;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
public class TemplateMarker {
    String identificator;
    @Nullable String name;
    @Nullable String lore;
    String iconId;
    String worldLocation;
    @Nullable Config data;
    boolean localMarker;
    @Nullable List<String> mapImageIds;
    @Setter
    @Nullable List<String> actionList;
    @Setter
    boolean draw;

    public TemplateMarker(String identificator, @Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable Config data, boolean localMarker, @Nullable List<String> mapImadeIds, @Nullable List<String> actionList, boolean draw) {
        this.identificator = identificator;
        this.name = name;
        this.lore = lore;
        this.iconId = iconId;
        this.worldLocation = worldLocation;
        this.data = data;
        this.localMarker = localMarker;
        this.mapImageIds = mapImadeIds;
        this.actionList = actionList;
        this.draw = draw;
    }

    public TemplateMarker(@Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable Config data, boolean localMarker, @Nullable List<String> mapImageIds,
                          @Nullable List<String> actionList, boolean draw) {
        this((localMarker ? "L-" : "S-") + UUID.randomUUID(), name, lore, iconId, worldLocation,
                data, localMarker, mapImageIds, actionList, draw);
    }

    public TemplateMarker(String identificator, @Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable String data, boolean localMarker, @Nullable String mapImageIds, @Nullable String actionList, boolean draw) {
        this.identificator = identificator;
        this.name = name;
        this.lore = lore;
        this.iconId = iconId;
        this.worldLocation = worldLocation;

        // Проверяем переданные данные
        this.data = (data != null && !data.equals("null")) ? GtwAPI.getInstance().getGtwMod().getConfigManager().createConfigFromString(data, ConfigType.YAML) : null;
        this.mapImageIds = (mapImageIds != null && !mapImageIds.isEmpty() && !mapImageIds.equals("null")) ? Arrays.asList(mapImageIds.split(";")) : null;
        this.actionList = (actionList != null && !actionList.isEmpty() && !actionList.equals("null")) ? Arrays.asList(actionList.split("ъ")) : null;

        this.localMarker = localMarker;
        this.draw = draw;
    }

    public TemplateMarker(@Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable String data, boolean localMarker, @Nullable String mapImageIds, @Nullable String actionList, boolean draw) {
        this((localMarker ? "L-" : "S-") + UUID.randomUUID(), name, lore, iconId, worldLocation,
                data, localMarker, mapImageIds, actionList, draw);
    }

    public void updateData(TemplateMarker updateTemplateData) {
        this.name = updateTemplateData.getName();
        this.lore = updateTemplateData.getLore();
        this.iconId = updateTemplateData.getIconId();
        this.worldLocation = updateTemplateData.getWorldLocation();
        this.data = updateTemplateData.getData();
        this.mapImageIds = updateTemplateData.getMapImageIds();
        this.actionList = updateTemplateData.getActionList();
        this.draw = updateTemplateData.isDraw();
        this.localMarker = updateTemplateData.isLocalMarker();
        this.identificator = updateTemplateData.getIdentificator();
    }

    public @Nullable String getMapImageIdsString() {
        return (mapImageIds!= null &&!mapImageIds.isEmpty())? String.join(";", mapImageIds) : null;
    }

    public @Nullable String getActionListString() {
        return (actionList!= null &&!actionList.isEmpty())? String.join("ъ", actionList) : null;
    }

    public int isDrawInt() {
        return draw? 1 : 0;
    }

    public void setMapImageIds(String var) {
        this.mapImageIds = (var != null && !var.isEmpty() && !var.equals("null")) ? Arrays.asList(var.split(";")) : null;
    }

    public void setMapImageIds(List<String> var) {
        this.mapImageIds = var;
    }

    public void setActionList(String var) {
        this.actionList = (var != null && !var.isEmpty() && !var.equals("null")) ? Arrays.asList(var.split("ъ")) : null;
    }

    public void setData(String data) {
        this.data = (data != null && !data.equals("null")) ? GtwAPI.getInstance().getGtwMod().getConfigManager().createConfigFromString(data, ConfigType.YAML) : null;
    }


}
