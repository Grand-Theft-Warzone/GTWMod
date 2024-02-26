package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.database.StorageManager;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.MapData;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GtwServerMapManager implements AtumModService {

    @Getter
    private Map<String, MapData> maps = new HashMap<>();

    @Getter
    private Config config;

    @Getter
    private String defaultGlobalmap;
    @Getter
    private String defaultMinimap;
    @Getter
    private boolean enableMap;
    @Getter
    private boolean debug;

    public GtwServerMapManager(AtumMod atumMod) {
        atumMod.provideModService(this);
    }

    public void initConfig() {
        config = GtwAPI.getInstance().getGtwMod().getConfigManager()
                .createLoadableConfig(
                        "config",
                        "map",
                        ConfigType.YAML,
                        true
                );
        debug = config.getBool("debug");
        if (debug) GtwLog.getLogger().info("[DEBUG Map] Start init map");
        defaultGlobalmap = config.getString("default_globalmap");
        defaultMinimap = config.getString("default_minimap");
        enableMap = config.getBool("enableMap");

        Config mapSection = config.getSubsection("maps");
        for (String mapId : mapSection.getKeys(false)) {
            Config cfg = mapSection.getSubsection(mapId);
            String imageId = cfg.getString("image_id");
            String attachedTo = cfg.getString("attachedTo");
            boolean allowLocalMarker = cfg.getBool("allow_local_marker");

            List<String> perm = (List<String>) cfg.get("permission");

            MapLocation topRight = new MapLocation(cfg.getSubsection("coordinates").getString("top_right"));
            MapLocation downRight = new MapLocation(cfg.getSubsection("coordinates").getString("down_right"));
            MapLocation downLeft = new MapLocation(cfg.getSubsection("coordinates").getString("down_right"));
            MapLocation topLeft = new MapLocation(cfg.getSubsection("coordinates").getString("top_right"));

            String colorBackgroundStr = cfg.getString("color_background");
            String colorBorderReachStr = cfg.getString("colorBorderReach");
            boolean isColorBackground = true, isColorBorderReach = true;
            AtumColor colorBackground = AtumColor.BLACK, colorBorderReach = AtumColor.BLACK;
            if (colorBackgroundStr.equals("0")) {
                isColorBackground = false;
            } else if (colorBackgroundStr.startsWith("#")) {
                colorBackground = AtumColor.fromHex(colorBackgroundStr);
            }

            if (colorBorderReachStr.equals("0")) {
                isColorBorderReach = false;
            } else if (colorBorderReachStr.startsWith("#")) {
                colorBorderReach = AtumColor.fromHex(colorBorderReachStr);
            }

            int minZoom = cfg.getIntOrDefault("min_zoom", 180);
            int maxZoom = cfg.getIntOrDefault("max_zoom", 1000);

            MapData data = new MapData(mapId, imageId, attachedTo, allowLocalMarker, perm, topRight, downRight, downLeft, topLeft, isColorBackground, colorBackground, isColorBorderReach, colorBorderReach, minZoom, maxZoom);
            maps.put(mapId, data);
            if (debug) {
                GtwLog.getLogger().info("\n============ " + mapId + " =============\n" +
                        "" +
                        "ImageID: " + imageId + "" +
                        "\nAttached To: " + attachedTo +
                        "\nAllow Local Marker: " + allowLocalMarker +
                        "\nPermission: " + perm +
                        "\nCoordimates: " +
                        "\n+-+ Top Right: " + topRight.getX() + "  " + topRight.getY() + "  " + topRight.getZ() +
                        "\n+-+ Down Right: " + downRight.getX() + "  " + downRight.getY() + "  " + downRight.getZ() +
                        "\n+-+ Down Left: " + downLeft.getX() + "  " + downLeft.getY() + "  " + downLeft.getZ() +
                        "\n+-+ Top Left: " + topLeft.getX() + "  " + topLeft.getY() + "  " + topLeft.getZ() +
                        "\nColor Background: " + colorBackground.toHex(false) +
                        "\nColor BorderReach: " + colorBorderReach.toHex(false) +
                        "\nMin Zoom: " + minZoom +
                        "\nMax Zoom: " + maxZoom +
                        "\n==================================");
            }
        }

    }


    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLInitializationEvent) {
            initConfig();
            new StorageManager(config);
        }
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "map";
    }
}
