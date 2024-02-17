package com.grandtheftwarzone.gtwmod.api.gui.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.gui.minimap.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseDynamicMarker implements DynamicMarker {

    private EntityCord cordMarker;

    private List<MapCord> mapCords;

    private MapImage mapImage;

    private ResourceLocation icon;

    private double coeficent;

    private double step;

    public BaseDynamicMarker(EntityCord cordMarker, MapImage mapImage, ResourceLocation icon, double coeficent, double step) {
        this.cordMarker = cordMarker;
        this.mapImage = mapImage;
        this.icon = icon;
        this.coeficent = coeficent;
        this.step = step;
        this.mapCords = new ArrayList<>();
        this.mapCords.add(mapImage.calculateCoord(cordMarker.getX(), cordMarker.getZ()));
    }

    @Override
    public EntityCord getRealCord() {
        return this.cordMarker;
    }

    public MapCord getDynamicMapCord() {

        // Check for card
//        if (map.equals(this.mapImage)) {
            //
//        }

        List<MapCord> addMapCords = new ArrayList<>();

        MapCord startCord = this.mapCords.get(mapCords.size() - 1);
        MapCord endCord = mapImage.calculateCoord(cordMarker.getX(), cordMarker.getZ());

        if (Math.abs(endCord.getX() - startCord.getX()) > 15 || Math.abs(endCord.getY() - startCord.getY()) > 15) {
            this.mapCords.clear();
            this.mapCords.add(endCord);
            return getRemoveFirstCord();
        }

        MapCord processStartCord = this.mapCords.get(mapCords.size() - 1);
        MapCord raznica = new MapCord(((endCord.getX() - startCord.getX()) / coeficent), ((endCord.getY() - startCord.getY()) / coeficent));


        for (int i = 0; i < coeficent; i++) {
            if (Math.abs((startCord.getX() + raznica.getX()) - processStartCord.getX()) > step || Math.abs((startCord.getY() + raznica.getY()) - processStartCord.getY()) > step) {
                startCord.setX(startCord.getX() + raznica.getX());
                startCord.setY(startCord.getY() + raznica.getY());
                MapCord mapCord = new MapCord(startCord.getX(), startCord.getY());
                processStartCord = mapCord;
                addMapCords.add(mapCord);
            } else {
                startCord.setX(startCord.getX() + raznica.getX());
                startCord.setY(startCord.getY() + raznica.getY());
            }
        }
        this.mapCords.addAll(addMapCords);

        return getRemoveFirstCord();
    }

    private MapCord getRemoveFirstCord() {
        if (mapCords.size() > 1) {
            return mapCords.remove(0);
        } else if (mapCords.size() == 1) {
            return mapCords.get(0);
        } else {
            return null;
        }

    }


}
