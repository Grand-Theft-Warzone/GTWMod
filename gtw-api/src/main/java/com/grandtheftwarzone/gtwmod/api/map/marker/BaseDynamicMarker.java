package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class BaseDynamicMarker implements DynamicMarker {

    private EntityLocation worldLocationMarker;

    private List<MapLocation> mapInterpolations;

    private MapImage mapImage;

    private ResourceLocation icon;

    private double coefficient;

    private double step;

    public BaseDynamicMarker(EntityLocation worldLocationMarker, MapImage mapImage, ResourceLocation icon, double coefficient, double step) {
        this.worldLocationMarker = worldLocationMarker;
        this.mapImage = mapImage;
        this.icon = icon;
        this.coefficient = coefficient;
        this.step = step;
        this.mapInterpolations = new ArrayList<>();
        this.mapInterpolations.add(mapImage.calculateCoord(worldLocationMarker.getX(), worldLocationMarker.getZ()));
    }

    @Override
    public EntityLocation getWorldLocation() {
        return this.worldLocationMarker;
    }

    public MapLocation getCurrentMapLocation() {

        // Check for card
//        if (map.equals(this.mapImage)) {
            //
//        }

        List<MapLocation> addmapInterpolations = new ArrayList<>();

        MapLocation startLocation = this.mapInterpolations.get(mapInterpolations.size() - 1);
        MapLocation endLocation = mapImage.calculateCoord(worldLocationMarker.getX(), worldLocationMarker.getZ());

        if (Math.abs(endLocation.getX() - startLocation.getX()) > 15 || Math.abs(endLocation.getY() - startLocation.getY()) > 15) {
            this.mapInterpolations.clear();
            this.mapInterpolations.add(endLocation);
            return getRemoveFirstLocation();
        }

        MapLocation processStartLocation = this.mapInterpolations.get(mapInterpolations.size() - 1);
        MapLocation difference = new MapLocation(((endLocation.getX() - startLocation.getX()) / coefficient), ((endLocation.getY() - startLocation.getY()) / coefficient));


        for (int i = 0; i < coefficient; i++) {
            if (Math.abs((startLocation.getX() + difference.getX()) - processStartLocation.getX()) > step || Math.abs((startLocation.getY() + difference.getY()) - processStartLocation.getY()) > step) {
                startLocation.setX(startLocation.getX() + difference.getX());
                startLocation.setY(startLocation.getY() + difference.getY());
                MapLocation mapLocation = new MapLocation(startLocation.getX(), startLocation.getY());
                processStartLocation = mapLocation;
                addmapInterpolations.add(mapLocation);
            } else {
                startLocation.setX(startLocation.getX() + difference.getX());
                startLocation.setY(startLocation.getY() + difference.getY());
            }
        }
        this.mapInterpolations.addAll(addmapInterpolations);

        return getRemoveFirstLocation();
    }

    // ОТРЕДАЧИТЬ НАЗВАНИЕ!!!! МОЖЕТ ТЫ СТЕК УЖЕ НАЧНЁШЬ ИСПОЛЬЗОВАТЬ?  А?
    private MapLocation getRemoveFirstLocation() {

//        System.out.println(Arrays.toString(mapInterpolations.toArray()));

        if (mapInterpolations.size() > 1) {
            return mapInterpolations.remove(0);
        } else if (mapInterpolations.size() == 1) {
            return mapInterpolations.get(0);
        } else {
            return null;
        }

    }


}
