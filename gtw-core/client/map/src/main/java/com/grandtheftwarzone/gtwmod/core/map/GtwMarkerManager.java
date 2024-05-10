package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MarkerManager;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseStaticMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import lombok.Getter;
import me.phoenixra.atumconfig.api.ConfigOwner;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumconfig.core.config.AtumConfigSection;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GtwMarkerManager implements MarkerManager {

    @Getter
    private List<MapMarker> localMarkerList = new ArrayList<>();

    @Getter
    private List<MapMarker> serverMarkerList = new ArrayList<>();

    @Getter
    private Config configMarker;

    public GtwMarkerManager() {
        configMarker = GtwAPI.getInstance().getGtwMod().getConfigManager()
                .createLoadableConfig(
                        "markers",
                        "display",
                        ConfigType.YAML,
                        true
                );
        initLocalMarker();
    }

    public void initLocalMarker() {
        List<MapMarker> newLocalMarkerList = new ArrayList<>();

        Config markers = configMarker.getSubsection("markers");

        GtwLog.getLogger().debug("Init Local Marker");

        GtwLog.getLogger().debug("Markers:");
        for (String markerKey : markers.getKeys(false)) {
            Config marker = markers.getSubsection(markerKey);
            String identificator = markerKey;
            String name = marker.getString("name");
            String lore = marker.getString("lore");
            String iconId = marker.getString("iconId");
            String worldLocation = marker.getString("worldLocation");
            boolean localMarker = true;
            List<String> mapImageIds = (List<String>) marker.get("mapImageIds");
            System.out.println(mapImageIds);
            List<String> actionList = null;
            boolean draw = true;
            TemplateMarker templateMarker = new TemplateMarker(identificator, name, lore, iconId, worldLocation, localMarker, mapImageIds, actionList, draw);

            System.out.println("\n============================");
            System.out.println(templateMarker);
            System.out.println("\n============================\n");

            newLocalMarkerList.add(new BaseStaticMarker(templateMarker));
        }
        localMarkerList = newLocalMarkerList;
    }


    public void addLocalMarker(MapMarker marker) {
        MapMarker searchMarker = getMarker(marker.getIdentificator());
        if (searchMarker != null) {
            localMarkerList.remove(searchMarker);
        }
        localMarkerList.add(marker);
        updateLocalConfig();
    }

    public void removeLocalMarker(String indentificator) {
        localMarkerList.remove(getMarker(indentificator));
        updateLocalConfig();
    }

    public void updateLocalConfig() {
        Config newConfig = new AtumConfigSection((ConfigOwner) configMarker, ConfigType.YAML, null);

        for (MapMarker marker : localMarkerList) {
            Config config = new AtumConfigSection((ConfigOwner) newConfig, ConfigType.YAML, null);
            config.set("name", marker.getName());
            config.set("lore", marker.getLore());
            config.set("iconId", marker.getIconId());
            config.set("worldLocation", marker.getWorldLocation().toString());
            config.set("mapImageIds", marker.getMapImageIds());
            config.set("draw", marker.isDraw());
            newConfig.set(marker.getIdentificator(), config);
        }

        configMarker.set("markers", newConfig);
        try {
            ((LoadableConfig)configMarker).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public @Nullable MapMarker getMarker(String identificator) {
        if (!localMarkerList.isEmpty()) {
            for (MapMarker marker : localMarkerList) {
                if (marker.getIdentificator().equals(identificator)) {
                    return marker;
                }
            }

        }
        if (!serverMarkerList.isEmpty()) {
            for (MapMarker marker : serverMarkerList) {
                if (marker.getIdentificator().equals(identificator)) {
                    return marker;
                }
            }

        }
        return null;
    }


    public List<MapMarker> getMarkerList() {
        List<MapMarker> combinedList = new ArrayList<>();
        combinedList.addAll(localMarkerList);
        combinedList.addAll(serverMarkerList);
        return combinedList;
    }

}
