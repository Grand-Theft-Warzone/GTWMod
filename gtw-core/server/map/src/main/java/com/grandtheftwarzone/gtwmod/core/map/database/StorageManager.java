package com.grandtheftwarzone.gtwmod.core.map.database;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.GtwServerMapManager;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.PlayerData;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.PlayerHudData;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.StaticMarker;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.database.Database;
import me.phoenixra.atumodcore.api.database.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StorageManager {

    private Database core;
    private Config config;

    public StorageManager(Config config) {
        this.config = config;
        initializeDB();
    }

    private void initializeDB() {
        Config cfg = config.getSubsection("database");
        String name = cfg.getString("name");
        String location = cfg.getString("location");
        core = new SQLiteDatabase(GtwAPI.getInstance().getGtwMod(), name, location);

        if (core.checkConnection()) {
            GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&aDatabase successfully established connection :) &7(Type: SQLite)"));

            if (!core.existsTable("player") || !core.existsTable("playerData") || core.existsTable("StaticMarker")) {
                GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&eCreate table: player, playerData, StaticMarker"));

                String query = ("CREATE TABLE IF NOT EXISTS player (\n" +
                        "  UUID VARCHAR(36) PRIMARY KEY UNIQUE,\n" +
                        "  minimap_id VARCHAR(64),\n" +
                        "  global_id VARCHAR(64),\n" +
                        "  show_map BOOLEAN\n" +
                        ");");
                core.execute(query);

                query = ("CREATE TABLE IF NOT EXISTS playerHud (\n" +
                        "  UUID VARCHAR(36) PRIMARY KEY UNIQUE,\n" +
                        "  zoom INTEGER,\n" +
                        "  active BOOLEAN\n" +
                        ");");
                core.execute(query);

                query = ("CREATE TABLE IF NOT EXISTS StaticMarker (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name VARCHAR(64),\n" +
                        "    lore TEXT,\n" +
                        "    map_id VARCHAR,\n" +
                        "    cord TEXT,\n" +
                        "    icon_id VARCHAR(64),\n" +
                        "    permission TEXT\n" +
                        ");");
                core.execute(query);

                GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&aCreating successfully completed!"));
            }

        }
    }


    public @NotNull PlayerData getPlayerData(UUID uuid) {
        String query = "SELECT * FROM `player` WHERE UUID = '" +uuid+"';";

        boolean found = false;
        ResultSet result = core.select(query);

        try {
            if (!result.next()) {
                query = "insert into\n" +
                        "  `player` (`UUID`, `global_id`, `minimap_id`, `show_map`)\n" +
                        "values\n" +
                        "  ('" +uuid+ "', 'default', 'default', '1');";
                core.execute(query);
                return new PlayerData(uuid, "default", "default", true);
            }

            UUID tab_uuid = UUID.fromString(result.getString("UUID"));
            String table_minimapId = result.getString("minimap_id");
            String table_globalId = result.getString("global_id");
            boolean table_showMap = result.getBoolean("show_map");

            return new PlayerData(tab_uuid, table_minimapId, table_globalId, table_showMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public @Nullable PlayerHudData getPlayerHudData(UUID uuid) {
        String query = "SELECT * FROM `playerHud` WHERE UUID = '" +uuid+"';";

        ResultSet result = core.select(query);
        if (result==null) {
            return null;
        }

        try {
            if (!result.next()) {
                return null;
            }

            UUID tab_uuid = UUID.fromString(result.getString("UUID"));
            int tableZoom = result.getInt("zoom");
            boolean table_showMap = result.getBoolean("active");

            return new PlayerHudData(tab_uuid, tableZoom, table_showMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public @Nullable StaticMarker getStaticMarker(int id) {
        String query = "SELECT * FROM `StaticMarker` WHERE id = '" +id+"';";

        ResultSet result = core.select(query);
        if (result==null) {
            return null;
        }

        try {
            if (!result.next()) {
                return null;
            }

            Integer tab_id = result.getInt("id");
            String table_zoom = result.getString("name");
            String table_lore = result.getString("lore");
            String table_mapId = result.getString("lore");
            MapLocation table_cord = new MapLocation(result.getString("cord"));
            String table_iconId = result.getString("icon_id");
            String[] table_permission = result.getString("permission").split(";");

            return new StaticMarker(tab_id, table_zoom, table_lore, table_mapId, table_cord, table_iconId, table_permission);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public @Nullable StaticMarker getStaticMarker(MapLocation location) {
        String cord = location.toString();
        String query = "SELECT * FROM `StaticMarker` WHERE cord = '" +cord+"';";

        ResultSet result = core.select(query);
        if (result==null) {
            return null;
        }

        try {
            if (!result.next()) {
                return null;
            }

            Integer tab_id = result.getInt("id");
            String table_zoom = result.getString("name");
            String table_lore = result.getString("lore");
            String table_mapId = result.getString("lore");
            MapLocation table_cord = new MapLocation(result.getString("cord"));
            String table_iconId = result.getString("icon_id");
            String[] table_permission = result.getString("permission").split(";");

            return new StaticMarker(tab_id, table_zoom, table_lore, table_mapId, table_cord, table_iconId, table_permission);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void editPlayerData(UUID uuid, @NotNull PlayerData newPlayerData) {


        StringBuilder query = new StringBuilder("UPDATE `playerData` SET show_map '").append(newPlayerData.getGlobalId()).append("' ");
        if (newPlayerData.getMinimapId() != null) {
            query.append(", minimap_id '").append(newPlayerData.getMinimapId()).append("' ");
        }
        if (newPlayerData.getGlobalId() != null) {
            query.append(", globalmap_id '").append(newPlayerData.getGlobalId()).append("' ");
        }
        query.append("WHERE `playerData`.`uuid` = '").append(uuid).append("'; ");

        core.executeUpdate(query.toString(), true);

    }


}
