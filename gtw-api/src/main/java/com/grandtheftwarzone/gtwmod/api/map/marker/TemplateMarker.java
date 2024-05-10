package com.grandtheftwarzone.gtwmod.api.map.marker;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Data
public class TemplateMarker {
    String identificator;
    @Nullable String name;
    @Nullable String lore;
    String iconId;
    String worldLocation;
    boolean localMarker;
    @Nullable List<String> mapImageIds;
    @Nullable List<String> actionList;
    boolean draw;

    public TemplateMarker(String identificator, @Nullable String name, @Nullable String lore, String iconId, String worldLocation, boolean localMarker, @Nullable List<String> mapImadeIds, @Nullable List<String> actionList, boolean draw) {
        this.identificator = identificator;
        this.name = name;
        this.lore = lore;
        this.iconId = iconId;
        this.worldLocation = worldLocation;
        this.localMarker = localMarker;
        this.mapImageIds = mapImadeIds;
        this.actionList = actionList;
        this.draw = draw;
    }

    public TemplateMarker(@Nullable String name, @Nullable String lore, String iconId, String worldLocation, boolean localMarker, @Nullable List<String> mapImadeIds, @Nullable List<String> actionList, boolean draw) {
        if (localMarker) {
            this.identificator = "L-" + UUID.randomUUID();
        } else {
            this.identificator = "S-" + UUID.randomUUID();
        }
        this.name = name;
        this.lore = lore;
        this.iconId = iconId;
        this.worldLocation = worldLocation;
        this.localMarker = localMarker;
        this.mapImageIds = mapImadeIds;
        this.actionList = actionList;
        this.draw = draw;

    }
}
