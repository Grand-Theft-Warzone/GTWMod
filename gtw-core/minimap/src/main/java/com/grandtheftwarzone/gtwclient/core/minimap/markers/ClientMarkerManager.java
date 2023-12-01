package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import com.grandtheftwarzone.gtwclient.core.minimap.Minimap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientMarkerManager {
    private final HashMap<Integer, Marker> markers = new HashMap<>();

    public void syncMarkers(List<Marker> newMarkers) {
        this.markers.clear();

        for (Marker marker : newMarkers) markers.put(marker.getHash(), marker);
    }

    public ArrayList<Marker> getMinimapMarkers(Minimap minimap) {
        return getMarkers(minimap.getStartX(), minimap.getStartZ(), minimap.getEndX(), minimap.getEndZ());
    }

    public ArrayList<Marker> getMarkers(int startX, int startZ, int endX, int endZ) {
        ArrayList<Marker> m = new ArrayList<>();
        for (int i = startX; i <= endX; i++)
            for (int j = startZ; j <= endZ; j++)
                if (markers.containsKey(Marker.getHash(i, j)))
                    m.add(markers.get(Marker.getHash(i, j)));

        return m;
    }

    public boolean hasMarkerOverlap(Marker marker) {
        for (int x = marker.getPosX() - marker.getType().getTextureWidth(); x <= marker.getPosX() + marker.getType().getTextureWidth(); x++)
            for (int z = marker.getPosZ() - marker.getType().getTextureHeight(); z <= marker.getPosZ() + marker.getType().getTextureHeight(); z++)
                if (markers.containsKey(Marker.getHash(x, z)))
                    if (markers.get(Marker.getHash(x, z)).getType().isGlobal())
                        return true;

        return false;
    }
}
