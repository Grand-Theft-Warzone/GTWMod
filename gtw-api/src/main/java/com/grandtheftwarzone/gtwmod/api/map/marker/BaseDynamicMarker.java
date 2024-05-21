package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseDynamicMarker extends BaseStaticMarker {

    private List<MapLocation> mapInterpolations;


    @Getter @Setter
    private double coefficient = 1;

    @Getter @Setter
    private double step = 1;

    protected MapImage mapImage;


    public BaseDynamicMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, @Nullable String iconId, EntityLocation worldLocation, double coef, double step, @Nullable Config data, boolean localMarker, @Nullable List<String> mapImageIds, @Nullable List<String> actionList, boolean draw) {
        super(indentificator, name, lore, icon, iconId, worldLocation, data, localMarker, mapImageIds, actionList, draw);
        this.coefficient = coef;
        this.step = step;
        this.mapInterpolations = new ArrayList<>();
    }

    public BaseDynamicMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, @Nullable String iconId, EntityLocation worldLocation, double coef, double step, @Nullable Config data, boolean localMarker, @Nullable List<String> mapImageIds, @Nullable List<String> actionList) {
        this(indentificator, name, lore, icon, iconId, worldLocation, coef, step, data, localMarker, mapImageIds, actionList, true);
    }
    public BaseDynamicMarker(TemplateMarker templateMarker) {
        super(templateMarker);
        this.mapInterpolations = new ArrayList<>();
    }

    public BaseDynamicMarker(TemplateMarker templateMarker, double coef, double step) {
        super(templateMarker);
        this.coefficient = coef;
        this.step = step;
        this.mapInterpolations = new ArrayList<>();
    }



    @Override
    public MapLocation getMapLocation(String typeMap) {

        this.mapImage = getMapImage(typeMap);


        if (!(getMapImageIds() == null) && !getMapImageIds().contains(this.mapImage.getImageId())) {
            GtwLog.getLogger().error("[getMapLocation] Error! Displaying marker " + getIdentificator() + " is not allowed on canvas " + this.mapImage.getImageId());
            return new MapLocation(-999999999,-999999999,-404);
        }

        if (!isDraw()) {
            GtwLog.getLogger().error("[getMapLocation] Error! Display of token " + getIdentificator() + " is disabled.");
            return new MapLocation(-999999999,-999999999,-405);
        }

        if (this.mapInterpolations.isEmpty()) {
            this.mapInterpolations.add(mapImage.calculateImageCoord(getWorldLocation().getX(), getWorldLocation().getZ()));
        }


        List<MapLocation> addMapInterpolations = new ArrayList<>();

        MapLocation startLocation = this.mapInterpolations.get(mapInterpolations.size() - 1);
        MapLocation endLocation = mapImage.calculateImageCoord(getWorldLocation().getX(), getWorldLocation().getZ());

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
                addMapInterpolations.add(mapLocation);
            } else {
                startLocation.setX(startLocation.getX() + difference.getX());
                startLocation.setY(startLocation.getY() + difference.getY());
            }
        }
        this.mapInterpolations.addAll(addMapInterpolations);

        return getRemoveFirstLocation();
    }

    // ОТРЕДАЧИТЬ НАЗВАНИЕ!!!! МОЖЕТ ТЫ СТЕК УЖЕ НАЧНЁШЬ ИСПОЛЬЗОВАТЬ?  А?
    protected MapLocation getRemoveFirstLocation() {

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
