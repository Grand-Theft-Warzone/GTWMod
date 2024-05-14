package com.grandtheftwarzone.gtwmod.core.map.database;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.data.server.PlayerMapData;
import com.grandtheftwarzone.gtwmod.core.map.dataobject.PlayerHudData;
import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.database.Database;
import me.phoenixra.atumodcore.api.database.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageManager {

    private Database core;
    private final Config config;

    public StorageManager(Config config) {
        this.config = config;
        initializeDB();
    }

    private void initializeDB() {
        Config cfg = config.getSubsection("database");
        String name = cfg.getString("name");
        String location = cfg.getString("location");
        core = new SQLiteDatabase(GtwAPI.getInstance().getGtwMod(), name, location);

        if (core.checkConnection()) { //@TODO         optimize with String.format
            GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&e[GTWMap] &aDatabase successfully established connection :) &7(Type: SQLite)"));

            if (!core.existsTable("player") || !core.existsTable("playerData") || core.existsTable("Marker")) {
                GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&e[GTWMap] &eCreate table: player, playerData, Marker"));

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

                query = ("CREATE TABLE IF NOT EXISTS Marker (\n" +
                        "    identificator TEXT PRIMARY KEY,\n" +
                        "    name TEXT,\n" +
                        "    lore TEXT,\n" +
                        "    iconId TEXT,\n" +
                        "    worldLocation TEXT,\n" +
                        "    data TEXT,\n" +
                        "    mapImageIds TEXT,\n" +
                        "    permissions TEXT,\n" +
                        "    actionList TEXT,\n" +
                        "    draw INTEGER\n" +
                        ");");
                core.execute(query);

                GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&e[GTWMap] &aCreating successfully completed!"));
            }

        }
    }


    public @NotNull PlayerMapData getPlayerData(UUID uuid) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `player` WHERE UUID = '").append(uuid).append("';");

        boolean found = false;
        ResultSet result = core.select(query.toString());

        try { //@TODO         optimize with String.format
            if (!result.next()) {
                query = new StringBuilder();
                query.append("insert into").append(
                        "  `player` (`UUID`, `global_id`, `minimap_id`, `show_map`)").append(
                        "values").append(
                        "  ('").append(uuid).append("', 'default', 'default', '1');");
                core.execute(query.toString());
                return new PlayerMapData(uuid, "default", "default", true);
            }

            UUID tab_uuid = UUID.fromString(result.getString("UUID"));
            String table_minimapId = result.getString("minimap_id");
            String table_globalId = result.getString("global_id");
            boolean table_showMap = result.getBoolean("show_map");

            return new PlayerMapData(tab_uuid, table_minimapId, table_globalId, table_showMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public @Nullable PlayerHudData getPlayerHudData(UUID uuid) {
        StringBuilder query = new StringBuilder("SELECT * FROM `playerHud` WHERE UUID = '").append(uuid).append("';");

        ResultSet result = core.select(query.toString());
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

    // -----------------------------------------------------------------------------

    public @Nullable ServerMarker getMarkerFromId(String identificator) {
        String query = "SELECT * FROM Marker WHERE identificator = '" +  identificator + "';";
        ResultSet resultSet = core.select(query);
        try {
            if (resultSet != null && resultSet.next()) {
                String tab_id = resultSet.getString("identificator");
                String name = resultSet.getString("name");
                String lore = resultSet.getString("lore");
                String iconId = resultSet.getString("iconId");
                String worldLocation = resultSet.getString("worldLocation");
                String data = resultSet.getString("data");
                String mapImageIds = resultSet.getString("mapImageIds");
                String permissions = resultSet.getString("permissions");
                String actionList = resultSet.getString("actionList");
                boolean draw = resultSet.getBoolean("draw");

                return new ServerMarker(tab_id, name, lore, iconId, worldLocation, data, false, mapImageIds, permissions, actionList, draw);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ServerMarker> getAllMarkers() {
        List<ServerMarker> markers = new ArrayList<>();
        String query = "SELECT * FROM Marker";
        ResultSet resultSet = core.select(query);
        try {
            while (resultSet != null && resultSet.next()) {
                String tab_id = resultSet.getString("identificator");
                String name = resultSet.getString("name");
                String lore = resultSet.getString("lore");
                String iconId = resultSet.getString("iconId");
                String worldLocation = resultSet.getString("worldLocation");
                String data = resultSet.getString("data");
                String mapImageIds = resultSet.getString("mapImageIds");
                String permissions = resultSet.getString("permissions");
                String actionList = resultSet.getString("actionList");
                boolean draw = resultSet.getBoolean("draw");

                ServerMarker marker = new ServerMarker(tab_id, name, lore, iconId, worldLocation, data, false, mapImageIds, permissions, actionList, draw);
                markers.add(marker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return markers;
    }

    public void createOrUpdateMarker(ServerMarker marker) {
        String checkQuery = "SELECT COUNT(*) AS count FROM Marker WHERE identificator = '" + marker.getIdentificator() + "';";
        ResultSet checkResult = core.select(checkQuery);
        try {
            if (checkResult != null && checkResult.next()) {
                int count = checkResult.getInt("count");
                if (count > 0) {
                    // Если элемент с таким идентификатором уже существует, обновляем его
                    String updateQuery = "UPDATE Marker SET name = '"+ marker.getName() +
                            "', lore = '" + marker.getLore() +
                            "', iconId = '" + marker.getIconId() +
                            "', worldLocation = '" + marker.getWorldLocation() +
                            "', data = '" + (marker.getData() != null ? marker.getData().toPlaintext() : null) +
                            "', mapImageIds = '" + marker.getMapImageIdsString() +
                            "', permissions = '" + marker.getPermissionsString() +
                            "', actionList = '" + marker.getActionListString() +
                            "', draw = '" + marker.isDrawInt() + "' " +
                            "WHERE identificator = '" + marker.getIdentificator() + "'";
                    core.execute(updateQuery);
                } else {
                    // Если элемент с таким идентификатором не существует, создаем новый
                    String insertQuery = "INSERT INTO Marker (identificator, name, lore, iconId, worldLocation, " +
                            "data, mapImageIds, permissions, actionList, draw) " +
                            "VALUES ('" + marker.getIdentificator() +
                            "', '" + marker.getName() +
                            "', '" + marker.getLore()
                            + "', '" + marker.getIconId() +
                            "', '" + marker.getWorldLocation() +
                            "', '" + (marker.getData() != null ? marker.getData().toPlaintext() : null) +
                            "', '" + marker.getMapImageIdsString() +
                            "', '" + marker.getPermissionsString() +
                            "', '" + marker.getActionListString() +
                            "', '" + marker.isDrawInt() + "')";
                    core.execute(insertQuery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMarkerFromId(String identifier) {
        String query = "DELETE FROM Marker WHERE identificator = '" + identifier + "';";
        core.execute(query);
    }


    // ----------------------------------------------------------------------------------------------------------

    public void editPlayerData(UUID uuid, @NotNull PlayerMapData newPlayerData) {

        //@TODO         optimize with String.format
        StringBuilder query = new StringBuilder("UPDATE `playerData` SET show_map '").append(newPlayerData.getGlobalmapId()).append("' ");
        if (newPlayerData.getMinimapId() != null) {
            query.append(", minimap_id '").append(newPlayerData.getMinimapId()).append("' ");
        }
        if (newPlayerData.getGlobalmapId() != null) {
            query.append(", globalmap_id '").append(newPlayerData.getGlobalmapId()).append("' ");
        }
        query.append("WHERE `playerData`.`uuid` = '").append(uuid).append("'; ");

        core.executeUpdate(query.toString(), true);

    }

}
