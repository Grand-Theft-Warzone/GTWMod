package com.grandtheftwarzone.gtwmod.core.display.minimap.marker;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.minimap.DynamicMarker;
import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import com.grandtheftwarzone.gtwmod.api.misc.EntityCord;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import com.grandtheftwarzone.gtwmod.core.display.minimap.MapImage;
import lombok.Getter;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RadarPlayer implements DynamicMarker {

    @Getter
    EntityCord playerCord;

    @Getter
    List<MapCord> mapCords;

    @Getter
    MapImage mapImage;

    @Getter
    ResourceLocation icon;

    @Getter
    double coeficent;

    double step;

    public RadarPlayer(EntityCord playerCord, MapImage mapImage, ResourceLocation icon) {
        this.playerCord = playerCord;
        this.mapImage = mapImage;
        this.icon = icon;
        this.mapCords = new ArrayList<>();
        this.mapCords.add(mapImage.calculateCoord(playerCord.getX(), playerCord.getZ()));

    }
    public RadarPlayer(EntityCord playerCord, MapImage mapImage) {
        System.out.println(playerCord);
        System.out.println(mapImage);
        this.playerCord = playerCord;
        this.mapImage = mapImage;
        this.mapCords = new ArrayList<>();
        this.mapCords.add(mapImage.calculateCoord(playerCord.getX(), playerCord.getZ()));

    }

    public RadarPlayer(EntityCord playerCord, MapImage mapImage, double coef, double step) {
        System.out.println(playerCord);
        System.out.println(mapImage);
        this.playerCord = playerCord;
        this.step = step;
        this.coeficent = coef;
        this.mapImage = mapImage;
        this.mapCords = new ArrayList<>();
        this.mapCords.add(mapImage.calculateCoord(playerCord.getX(), playerCord.getZ()));

    }

    public RadarPlayer(EntityCord playerCord, MapImage mapImage, ResourceLocation icon, double coef, double step) {
        System.out.println(playerCord);
        System.out.println(mapImage);
        this.playerCord = playerCord;
        this.step = step;
        this.icon = icon;
        this.coeficent = coef;
        this.mapImage = mapImage;
        this.mapCords = new ArrayList<>();
        this.mapCords.add(mapImage.calculateCoord(playerCord.getX(), playerCord.getZ()));

    }


    public MapCord getDynamicMapCord(MapImage map) {

        // Check for card
        if (map.equals(this.mapImage)) {
            //
        }

        List<MapCord> addMapCords = new ArrayList<>();

        MapCord startCord = this.mapCords.get(mapCords.size() - 1);
        MapCord endCord = mapImage.calculateCoord(playerCord.getX(), playerCord.getZ());

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

    public void updateColorFilter() {
        float proximity = mapImage.proximityToTheBorder(playerCord.getX(), playerCord.getZ());  // 0.00 - 1.00
        float opacity = 0;
        if (proximity > 0.9) {
            opacity = (proximity - 0.9F) * 0.9F * 10;
        }

        GtwAPI.getInstance().getGtwMinimapManager().setColorFilter(new ColorFilter(AtumColor.BLACK, opacity));
    }

    public boolean inMap() {
        return this.mapImage.inRealMap(playerCord.getX(), playerCord.getZ());
    }


}
