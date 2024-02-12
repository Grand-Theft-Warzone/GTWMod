package com.grandtheftwarzone.gtwmod.api.gui.minimap;

import com.grandtheftwarzone.gtwmod.api.misc.MapCord;

import java.util.List;

public interface DynamicMarker extends StaticMarker {

    List<MapCord> mapCords = null;


    List<MapCord> getMapCords();


}
