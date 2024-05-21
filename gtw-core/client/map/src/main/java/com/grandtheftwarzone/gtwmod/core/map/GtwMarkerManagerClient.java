package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.manager.client.MarkerManagerClient;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseDynamicMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseStaticMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.PlayerMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.RadarClient;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumconfig.core.config.AtumConfigSection;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GtwMarkerManagerClient implements MarkerManagerClient {

    @Getter
    private List<MapMarker> localMarkerList = new ArrayList<>();

    @Getter @Setter
    private HashMap<String, MapMarker> serverMarkerMap = new HashMap<>();

    @Getter
    private Config configMarker;

    public GtwMarkerManagerClient() {
        configMarker = GtwAPI.getInstance().getGtwMod().getConfigManager()
                .createLoadableConfig(
                        "markers",
                        "сache",
                        ConfigType.JSON,
                        false
                );
        initLocalMarker();
    }

    public void initLocalMarker() {
        List<MapMarker> newLocalMarkerList = new ArrayList<>();

        Config markers = configMarker.getSubsectionOrNull("markers");
        if (markers == null) {
            localMarkerList.clear();
            return;
        }

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
            TemplateMarker templateMarker = new TemplateMarker(identificator, name, lore, iconId, worldLocation, null, localMarker, mapImageIds, actionList, draw);

            System.out.println("\n============================");
            System.out.println(templateMarker);
            System.out.println("\n============================\n");

            newLocalMarkerList.add(new BaseStaticMarker(templateMarker));
        }
        localMarkerList = newLocalMarkerList;
    }


    public void addLocalMarker(MapMarker marker, boolean save) {
        MapMarker searchMarker = getMarker(marker.getIdentificator());
        if (searchMarker != null) {
            localMarkerList.remove(searchMarker);
        }
        localMarkerList.add(marker);
        if (save) {
            updateLocalConfig();
        }
    }

    public void addLocalMarker(MapMarker marker) {
        this.addLocalMarker(marker, true);
    }

    public @Nullable MapMarker removeLocalMarker(String indentificator) {

        MapMarker removedMarker = getMarker(indentificator);

        if (removedMarker != null) {
            localMarkerList.remove(removedMarker);
        }
        updateLocalConfig();

        return  removedMarker;
    }

    public void updateLocalConfig() {



        Config newConfig = new AtumConfigSection(configMarker.getConfigOwner(), ConfigType.YAML, null);


        
        for (MapMarker marker : localMarkerList) {


            Config markerConfig = marker.serializeToConfig(configMarker.getConfigOwner(), null);

            String identificator = markerConfig.getKeys(false).get(0);
            newConfig.set(identificator, markerConfig.getSubsection(identificator));

        }
        configMarker.set("markers", newConfig);

        try {
            ((LoadableConfig)configMarker).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable MapMarker getMarker(String identificator) {

        MapMarker localMarker = getLocalMarker(identificator);
        if (localMarker!= null) {
            return localMarker;
        }

//        if (!serverMarkerList.isEmpty()) {
//            for (MapMarker marker : serverMarkerList) {
//                if (marker.getIdentificator().equals(identificator)) {
//                    return marker;
//                }
//            }
//
//        }
        return null;
    }

    public @Nullable MapMarker getServerMarker(String id) {
        return serverMarkerMap.getOrDefault(id, null);
    }


    public List<MapMarker> getServerMarkersList() {
        System.out.print("SML: " + serverMarkerMap);
        return new ArrayList<>(serverMarkerMap.values());
    }


    public @Nullable MapMarker getLocalMarker(String identificator) {
        if (!localMarkerList.isEmpty()) {
            for (MapMarker marker : localMarkerList) {
                if (marker.getIdentificator().equals(identificator)) {
                    return marker;
                }
            }

        }
        return null;
    }
    public void updateServerMarkers(List<TemplateMarker> markers) {

        HashMap<String, MapMarker> newServerMarkerMap = new HashMap<>();

        for (TemplateMarker templateMarker : markers) {
            String markerId = templateMarker.getIdentificator();
            MapMarker oldMarker = getServerMarker(markerId);
            MapMarker marekr;
            if (oldMarker != null) {
                oldMarker.update(templateMarker);
                newServerMarkerMap.put(markerId, oldMarker);
                continue;
            }

            String type = (templateMarker.getData() != null) ? templateMarker.getData().getString("type") : null;
            if (type == null) {
                marekr = new BaseStaticMarker(templateMarker);
            } else if (type.equals("player")) {
                marekr = new PlayerMarker(templateMarker);
            } else if (type.equals("dynamic")) {
                marekr = new BaseDynamicMarker(templateMarker);
            } else {
                marekr = new BaseStaticMarker(templateMarker);
            }
            newServerMarkerMap.put(markerId, marekr);

        }
        this.serverMarkerMap = newServerMarkerMap;
    }

    public List<MapMarker> getAllMarker() {
        List<MapMarker> combinedList = new ArrayList<>();
        combinedList.addAll(localMarkerList);
        combinedList.addAll(getServerMarkersList());
        return combinedList;
    }

    public List<MapMarker> getAllMarkerFilter(String mapImageId) {
        List<MapMarker> combinedList = new ArrayList<>();
        combinedList.addAll(localMarkerList);
        combinedList.addAll(getServerMarkersList());
        List<MapMarker> filteredList = new ArrayList<>();
        for (MapMarker marker : combinedList) {
            if (marker.getMapImageIds() == null || marker.getMapImageIds().contains(mapImageId)) {
                filteredList.add(marker);
            }
        }
        return filteredList;
    }


}
