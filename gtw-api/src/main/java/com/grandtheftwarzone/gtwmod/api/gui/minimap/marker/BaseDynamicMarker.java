package com.grandtheftwarzone.gtwmod.api.gui.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.gui.minimap.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseDynamicMarker implements DynamicMarker {

    private EntityLocation worldLocationMarker;

    private List<MapLocation> mapLocations;

    private MapImage mapImage;

    private ResourceLocation icon;

    private double coeficent;

    private double step;

    public BaseDynamicMarker(EntityLocation worldLocationMarker, MapImage mapImage, ResourceLocation icon, double coeficent, double step) {
        this.worldLocationMarker = worldLocationMarker;
        this.mapImage = mapImage;
        this.icon = icon;
        this.coeficent = coeficent;
        this.step = step;
        this.mapLocations = new ArrayList<>();
        this.mapLocations.add(mapImage.calculateCoord(worldLocationMarker.getX(), worldLocationMarker.getZ()));
    }

    @Override
    public EntityLocation getWorldLocation() {
        return this.worldLocationMarker;
    }

    public MapLocation getDynamicMapLocation() {

        // Check for card
//        if (map.equals(this.mapImage)) {
            //
//        }

        List<MapLocation> addMapLocations = new ArrayList<>();

        MapLocation startLocation = this.mapLocations.get(mapLocations.size() - 1);
        MapLocation endLocation = mapImage.calculateCoord(worldLocationMarker.getX(), worldLocationMarker.getZ());

        if (Math.abs(endLocation.getX() - startLocation.getX()) > 15 || Math.abs(endLocation.getY() - startLocation.getY()) > 15) {
            this.mapLocations.clear();
            this.mapLocations.add(endLocation);
            return getRemoveFirstLocation();
        }

        MapLocation processStartLocation = this.mapLocations.get(mapLocations.size() - 1);
        MapLocation raznica = new MapLocation(((endLocation.getX() - startLocation.getX()) / coeficent), ((endLocation.getY() - startLocation.getY()) / coeficent));


        for (int i = 0; i < coeficent; i++) {
            if (Math.abs((startLocation.getX() + raznica.getX()) - processStartLocation.getX()) > step || Math.abs((startLocation.getY() + raznica.getY()) - processStartLocation.getY()) > step) {
                startLocation.setX(startLocation.getX() + raznica.getX());
                startLocation.setY(startLocation.getY() + raznica.getY());
                MapLocation mapLocation = new MapLocation(startLocation.getX(), startLocation.getY());
                processStartLocation = mapLocation;
                addMapLocations.add(mapLocation);
            } else {
                startLocation.setX(startLocation.getX() + raznica.getX());
                startLocation.setY(startLocation.getY() + raznica.getY());
            }
        }
        this.mapLocations.addAll(addMapLocations);

        return getRemoveFirstLocation();
    }

    private MapLocation getRemoveFirstLocation() {
        if (mapLocations.size() > 1) {
            return mapLocations.remove(0);
        } else if (mapLocations.size() == 1) {
            return mapLocations.get(0);
        } else {
            return null;
        }

    }


}
