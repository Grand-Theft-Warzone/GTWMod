package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.core.minimap.Minimap;
import com.grandtheftwarzone.gtwclient.core.minimap.packets.PacketMarkerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarkerManager {

    //TODO: Maybe use a list of marker instead of a single marker per location (hash)
    //TODO: Maybe use a quadtree
    private final HashMap<Integer, Marker> markers = new HashMap<>();
    private final NetworkManager n;

    public MarkerManager(NetworkManager networkManager) {
        n = networkManager;
    }

    public void updateMarkers(List<Marker> markers) {
        this.markers.clear();

        for (Marker marker : markers)
            insertMarker(marker);
    }

    public void insertMarker(Marker marker) {
        markers.put(getHash(marker.getPosX(), marker.getPosZ()), marker);
    }

    public void removeMarker(Marker marker) {
        markers.remove(getHash(marker.getPosX(), marker.getPosZ()));
    }

    public List<Marker> queryRange(double startX, double startZ, double endX, double endZ) {
        List<Marker> m = new ArrayList<>();

        for (int i = (int) startX; i <= endX; i++)
            for (int j = (int) startZ; j <= endZ; j++)
                if (markers.containsKey(getHash(i, j))) m.add(markers.get(getHash(i, j)));
        return m;
    }

    @SideOnly(Side.CLIENT)
    public List<Marker> queryMinimap(Minimap minimap) {
        return queryRange(minimap.getStartX(), minimap.getStartZ(), minimap.getEndX(), minimap.getEndZ());
    }

    @SideOnly(Side.SERVER)
    public void syncMarkers() {
        n.sendToAll(new PacketMarkerData(markers.values()));
    }

    @SideOnly(Side.SERVER)
    public void sendMarkers(EntityPlayer player) {
        player.sendMessage(new TextComponentString("Sending markers: "+ markers.size()));
        EntityPlayerMP p = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(player.getUniqueID());
        n.sendTo(new PacketMarkerData(markers.values()), p);
    }

    private int getHash(double x, double z) {
        return (int) (x * 31 + z * 17); //TODO: Improve hash
    }

}
