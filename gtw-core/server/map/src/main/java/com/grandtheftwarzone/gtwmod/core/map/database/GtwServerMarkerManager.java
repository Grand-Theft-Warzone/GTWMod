package com.grandtheftwarzone.gtwmod.core.map.database;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.manager.server.MarkerManagerServer;
import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GtwServerMarkerManager implements MarkerManagerServer {

    @Getter
    private StorageManager storageManager;

    private List<ServerMarker> serverMarkerList = new ArrayList<>();

    public GtwServerMarkerManager(StorageManager storageManager) {
        this.storageManager = storageManager;
        initMarker();
    }

    public void initMarker() {
        List<ServerMarker> dbServerMarkers = storageManager.getAllMarkers();

        serverMarkerList = dbServerMarkers;
        GtwLog.getLogger().info(StringUtils.formatMinecraftColors("&e[GTWMap] &aMarkers loaded. " + dbServerMarkers.size() + " markers are displayed."));
    }

    public @Nullable ServerMarker removeMarker(String identificator) {

        ServerMarker removedMarker = getMarker(identificator);

        if (removedMarker!= null) {
            storageManager.removeMarkerFromId(removedMarker.getIdentificator());
            serverMarkerList.remove(removedMarker);
        }

        return removedMarker;
    }


    public void createOrUpdateMarker(ServerMarker serverMarker) {
        String updateServerMarkerId = serverMarker.getIdentificator();
        ServerMarker oldServerMarker = getMarker(updateServerMarkerId);
        if (oldServerMarker == null) {
            storageManager.createOrUpdateMarker(serverMarker);
            serverMarkerList.add(serverMarker);
            return;
        }

        oldServerMarker.updateData(serverMarker);
        storageManager.createOrUpdateMarker(serverMarker);
    }

    public @Nullable ServerMarker getMarker(String identificator) {
        if (!serverMarkerList.isEmpty()) {

            for (ServerMarker marker : serverMarkerList) {
                if (marker.getIdentificator().equals(identificator)) {
                    return marker;
                }
            }
        }
        return null;
    }


    public List<ServerMarker> getAllMarker() {
        return new ArrayList<>(serverMarkerList);

    }

    public @Nullable List<ServerMarker> getMarkerFilterUUID(UUID uuid) {
        List<ServerMarker> markers = new ArrayList<>();

        EntityPlayer player = GtwAPI.getInstance().getServer().getEntityWorld().getPlayerEntityByUUID(uuid);


        if (player == null) {
            return null;
        }

        for (ServerMarker marker : serverMarkerList) {
            List<String> permis = marker.getPermissions();
            if (permis == null || permis.isEmpty()) {
                markers.add(marker);
                continue;
            }

            for (String perm : permis) {
                if (PermissionAPI.hasPermission(player, perm)) {
                    markers.add(marker);
                    break;
                }
            }
        }

        return markers;
    }

    public List<ServerMarker> getAllPlayerMarker() {

        List<ServerMarker> markers = new ArrayList<>();

        List<EntityPlayerMP> players = GtwAPI.getInstance().getServer().getPlayerList().getPlayers();

        for (EntityPlayerMP playerMP : players) {
            // Сюда

            String id = "SP-" + playerMP.getUniqueID();

            /*
            Структура player Config:
            type: player
            data:
            - player_name: playerName
            - world: world_name
            - pos_x: posX
            - pos_y: posY
            - pos_z: posZ
            - pos_yaw: posYaw
            - pos_pitch: posPitch
             */

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", "player");
            hashMap.put("data", new HashMap<String, Object>() {{
                put("player_name", playerMP.getName());
                put("world", playerMP.world.getWorldInfo().getWorldName());
                put("pos_x", playerMP.posX);
                put("pos_y", playerMP.posY);
                put("pos_z", playerMP.posZ);
                put("pos_yaw", playerMP.cameraYaw);
                put("pos_pitch", playerMP.cameraPitch);
            }});

            Config config = GtwAPI.getInstance().getGtwMod().getConfigManager().createConfig(hashMap, ConfigType.YAML);
            EntityLocation location = new EntityLocation(playerMP.posX, playerMP.posZ, playerMP.posY, playerMP.rotationYaw, playerMP.rotationPitch);
            ServerMarker marker = new ServerMarker(id, playerMP.getName(), null, "@player_head=" + playerMP.getName(), location.toString(), config.toPlaintext(), null, null, true);
            markers.add(marker);
        }

        return markers;

    }

}
