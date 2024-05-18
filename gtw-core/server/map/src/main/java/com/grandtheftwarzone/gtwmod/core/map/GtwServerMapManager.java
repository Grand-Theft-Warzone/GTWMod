package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.manager.server.MapManagerServer;
import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersServer;
import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.database.GtwServerMarkerManager;
import com.grandtheftwarzone.gtwmod.core.map.database.StorageManager;
import com.grandtheftwarzone.gtwmod.api.map.data.RestrictionsData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.MapData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.PlayerMapData;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType.INTERVAL;

public class GtwServerMapManager implements AtumModService, MapManagerServer {

    @Getter
    private Map<String, MapData> maps = new HashMap<>();

    @Getter
    private Config config;

    @Getter
    private String defaultGlobalmapId;
    @Getter
    private String defaultMinimapId;
    @Getter
    private boolean enableMap;
    @Getter
    private boolean debug;
    @Getter
    private StorageManager db;
    @Getter
    private GtwServerMarkerManager markerManager;

    @Getter
    private MapConsumersServer mapConsumers;

    @Getter
    private ProcessConsumer processConsumer;

    public GtwServerMapManager(AtumMod atumMod) {
        atumMod.provideModService(this);
        this.mapConsumers = new MapConsumersServer();
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
        defaultGlobalmapId = config.getString("default_globalmap");
        defaultMinimapId = config.getString("default_minimap");
        enableMap = config.getBool("enable_map");

        Config mapSection = config.getSubsection("maps");
        for (String mapId : mapSection.getKeys(false)) {
            Config cfg = mapSection.getSubsection(mapId);
            String imageId = cfg.getString("image_id");
            String attachedTo = cfg.getString("attached_to");
            boolean allowLocalMarker = cfg.getBool("allow_local_marker");

            List<String> perm = (List<String>) cfg.get("permission");

            MapLocation topRight = new MapLocation(cfg.getSubsection("coordinates").getString("top_right"));
            MapLocation downRight = new MapLocation(cfg.getSubsection("coordinates").getString("down_right"));
            MapLocation downLeft = new MapLocation(cfg.getSubsection("coordinates").getString("down_left"));
            MapLocation topLeft = new MapLocation(cfg.getSubsection("coordinates").getString("top_left"));

            int offsetX = cfg.getIntOrDefault("offsetX", 0);
            int offsetY = cfg.getIntOrDefault("offsetY", 0);

            String colorBackgroundStr = cfg.getString("color_background");
            String colorBorderReachStr = cfg.getString("color_borderReach");
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

            Integer minZoom = cfg.getIntOrNull("min_zoom");
            Integer maxZoom = cfg.getIntOrNull("max_zoom");

            MapData data = new MapData(mapId, imageId, attachedTo, allowLocalMarker, perm, topRight, downRight, downLeft, topLeft, offsetX, offsetY, isColorBackground, colorBackground, isColorBorderReach, colorBorderReach, minZoom, maxZoom);
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
                        "\nOffset: " + offsetX + " " + offsetY +
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
            onInit((FMLInitializationEvent) fmlEvent);
        }
    }

    public void onInit(FMLInitializationEvent event) {
        initConfig();
        this.db = new StorageManager(config);
        this.markerManager = new GtwServerMarkerManager(this.db);
        this.processConsumer = new ProcessConsumer();

    }


//    @SubscribeEvent
//    public void onPlayerChat(ServerChatEvent event) {
//        if (event.getMessage().equalsIgnoreCase("qq")) {
//            GtwAPI.getInstance().getNetworkAPI().sendNotification(new NotificationRequest("hi", 123), UUID.fromString("120abf41-c686-3a55-8362-5f06e763dbbf"));
//        }
//    }

    // -------------------

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        List<EntityPlayerMP> players = GtwAPI.getInstance().getServer().getPlayerList().getPlayers();

        for (EntityPlayerMP playerMP : players) {
            List<ServerMarker> markers = new ArrayList<>();
            List<ServerMarker> serverMarkers = this.markerManager.getMarkerFilterUUID(playerMP.getUniqueID());
            List<ServerMarker> playerMarkers = this.markerManager.getAllPlayerMarker();
            if (serverMarkers != null) {
                markers.addAll(serverMarkers);
            }
            if (playerMarkers != null) {
                markers.addAll(playerMarkers);
            }
            // Отправляем markers
        }

    }

    private boolean checkAllowDisplay(UUID uuid) {
        return db.getPlayerData(uuid).isAllowMapDisplay() && enableMap;
    }
    private boolean checkAllowDisplay(PlayerMapData playerData) {
        return playerData.isAllowMapDisplay() && enableMap;
    }

    private boolean checkPermissions(UUID uuid, MapData mapData, String attachedTo) {
        if ( !(mapData.getAttachedTo().equals("all") || attachedTo.equals(mapData.getAttachedTo()))) {
            System.out.println(attachedTo + " " + mapData.getAttachedTo());
            return false;
        }
        EntityPlayer player = GtwAPI.getInstance().getServer().getEntityWorld().getPlayerEntityByUUID(uuid);

        if (player == null) {
            return false;
        }

        for (String perm : mapData.getPermissions()) {
            if (!PermissionAPI.hasPermission(player, perm)) {
                System.out.println("право " + perm + " " + PermissionAPI.hasPermission(player, perm));
                System.out.println("Право " + perm + " не обнаружено.");
                return false;
            }
        }
        return true;
    }

    @Nullable
    public MapData getMapData(UUID uuid, String attachedTo, @Nullable String mapId) {
        MapData mapData = getMaps().getOrDefault(mapId, null);

        if (mapData == null) {
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBB");
            return null;
        }

        if (!checkAllowDisplay(uuid)) {
            System.out.println("AAAAAAAAAAAAAAAAAAAa");
            return null;
        }
        if (!checkPermissions(uuid, mapData, attachedTo)) {
            System.out.println("! At the user " + GtwAPI.getInstance().getServer().getPlayerList().getPlayerByUUID(uuid).getName() + "no rights to the card " + mapId);
            return null;
        }

        return mapData;
    }

    public PlayerMapData getPlayerData(UUID uuid) {
        return db.getPlayerData(uuid);
    }

    public RestrictionsData getRestrictionsData(UUID uuid) {
        PlayerMapData playerData = getPlayerData(uuid);
        MapData minimapData = maps.get(playerData.getMinimapId());
        return new RestrictionsData(uuid, checkAllowDisplay(playerData), minimapData.isAllowLocalMarker(), minimapData.getMinZoomOrDefault(), minimapData.getMaxZoomOrDefault());
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
