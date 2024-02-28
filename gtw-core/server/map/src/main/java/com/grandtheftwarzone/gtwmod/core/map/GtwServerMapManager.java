package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapManagerServer;
import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumers;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.database.StorageManager;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.RestrictionsData;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.MapData;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.PlayerData;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GtwServerMapManager implements AtumModService, MapManagerServer {

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
    @Getter
    private StorageManager db;

    @Getter
    private MapConsumers mapConsumers;

    @Getter
    private ProcessConsumer processConsumer;

    public GtwServerMapManager(AtumMod atumMod) {
        atumMod.provideModService(this);
        this.mapConsumers = new MapConsumers();
        this.processConsumer = new ProcessConsumer();
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
            this.db = new StorageManager(config);
        }
    }

    // -------------------

    private boolean checkAllowDisplay(UUID uuid) {
        return db.getPlayerData(uuid).isAllowMapDisplay() && enableMap;
    }
    private boolean checkAllowDisplay(PlayerData playerData) {
        return playerData.isAllowMapDisplay() && enableMap;
    }

    private boolean checkPermissions(UUID uuid, MapData mapData, String attachedTo) {
        if (!attachedTo.equals(mapData.getAttachedTo())) {
            return false;
        }
        EntityPlayer player = GtwAPI.getInstance().getServer().getEntityWorld().getPlayerEntityByUUID(uuid);

        if (player == null) {
            return false;
        }

        for (String perm : mapData.getPermissions()) {
            if (!PermissionAPI.hasPermission(player, perm)) {
                return false;
            }
        }
        return true;
    }

    private @Nullable MapData getMapData(UUID uuid, String attachedTo, String mapId) {
        MapData mapData = getMaps().getOrDefault(mapId, null);

        if (mapData == null) {
            return null;
        }

        if (!checkAllowDisplay(uuid)) {
            return null;
        }
        if (!checkPermissions(uuid, mapData, attachedTo)) {
            return null;
        }

        return mapData;
    }

    private @NotNull PlayerData getPlayerData(UUID uuid) {
        PlayerData playerData = db.getPlayerData(uuid);
        if (playerData.getMinimapId().equals("default")) {
            playerData.setMinimapId(defaultMinimap);
        }
        if (playerData.getGlobalId().equals("default")) {
            playerData.setGlobalId(defaultGlobalmap);
        }
        return playerData;
    }

    private RestrictionsData getRestrictionsData(UUID uuid) {
        PlayerData playerData = getPlayerData(uuid);
        MapData minimapData = maps.get(playerData.getMinimapId());
        return new RestrictionsData(uuid, checkAllowDisplay(playerData), minimapData.getMinZoom(), minimapData.getDefaultMaxZoom());
    }

    // -------------------

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "map";
    }


}
