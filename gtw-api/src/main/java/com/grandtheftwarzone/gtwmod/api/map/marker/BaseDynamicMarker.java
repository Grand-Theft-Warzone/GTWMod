package com.grandtheftwarzone.gtwmod.api.map.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class BaseDynamicMarker implements MapMarker {

    private String identificator;

    private ResourceLocation icon;

    private String name;

    private String lore;

    private List<String> mapImageIds;

    private boolean localMarker;

    @Setter
    private boolean draw;

    private List<String> actionList;

    private EntityLocation worldLocation;

    // -------------

    private List<MapLocation> mapInterpolations;


    private double coefficient;

    private double step;

    protected MapImage mapImage;


    public BaseDynamicMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, EntityLocation worldLocation, double coef, double step, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList, boolean draw) {
        this.identificator = indentificator;
        this.name = name;
        this.lore = lore;
        this.icon = icon;
        this.worldLocation = worldLocation;
        this.coefficient = coef;
        this.step = step;
        this.localMarker = localMarker;
        this.mapImageIds = mapImageIds;
        this.actionList = actionList;
        this.draw = draw;
        this.mapInterpolations = new ArrayList<>();
        this.mapInterpolations.add(mapImage.calculateImageCoord(worldLocation.getX(), worldLocation.getZ()));
    }

    public BaseDynamicMarker(String indentificator, @Nullable String name, @Nullable String lore, ResourceLocation icon, EntityLocation worldLocation, double coef, double step, boolean localMarker, List<String> mapImageIds, @Nullable List<String> actionList) {
        this(indentificator, name, lore, icon, worldLocation, coef, step, localMarker, mapImageIds, actionList, true);
    }


    public MapLocation getMapLocation(String targetMap) {

        this.mapImage = getMapImage(targetMap);


        if (!this.mapImageIds.contains(this.mapImage.getImageId())) {
            GtwLog.getLogger().error("[getMapLocation] Error!");
            return new MapLocation(-999999999,-999999999,-404);
        }

        if (!draw) {
            GtwLog.getLogger().error("[getMapLocation] Error! draw disable.");
            return new MapLocation(-999999999,-999999999,-405);
        }


        List<MapLocation> addMapInterpolations = new ArrayList<>();

        MapLocation startLocation = this.mapInterpolations.get(mapInterpolations.size() - 1);
        MapLocation endLocation = mapImage.calculateImageCoord(worldLocation.getX(), worldLocation.getZ());

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

    public MapImage getMapImage(String targetMap) {
        if (Objects.equals(targetMap, "minimap")) {
            return GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage();
        } else if (Objects.equals(targetMap, "globalmap")) {
            return GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().getGlobalmapImage();
        }
        return null;
    }

    public MapLocation getMapLocation() {
        return this.getMapLocation("globalmap");
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
