package com.grandtheftwarzone.gtwmod.core.display.minimap;

import com.grandtheftwarzone.gtwmod.api.minimap.IClientMarkerManager;
import com.grandtheftwarzone.gtwmod.api.minimap.Marker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientMarkerManager implements IClientMarkerManager {
    //TODO: Check the best way to store markers
    private final ArrayList<Marker> markers = new ArrayList<>();

    public void syncMarkers(List<Marker> newMarkers) {
        this.markers.clear();
        this.markers.addAll(newMarkers);
    }

    public ArrayList<Marker> getMarkers(double startX, double startZ, double endX, double endZ) {
        ArrayList<Marker> m = new ArrayList<>();

        for (Marker marker : markers)
            if (marker.getPosX() >= startX && marker.getPosX() <= endX)
                if (marker.getPosZ() >= startZ && marker.getPosZ() <= endZ)
                    m.add(marker);

        return m;
    }

    public boolean hasMarkerOverlap(Marker marker) {
        int hW = marker.getType().getAtlas().getWidth() / 2;
        int hH = marker.getType().getAtlas().getHeight() / 2;

        for (Marker m : markers)
            if (m != null && !m.getType().isGlobal() && m != marker) {
                int hW2 = m.getType().getAtlas().getWidth() / 2;
                int hH2 = m.getType().getAtlas().getHeight() / 2;

                if (marker.getPosX() - hW <= m.getPosX() + hW2 && marker.getPosX() + hW >= m.getPosX() - hW2)
                    if (marker.getPosZ() - hH <= m.getPosZ() + hH2 && marker.getPosZ() + hH >= m.getPosZ() - hH2)
                        return true;
            }

        return false;
    }
}
