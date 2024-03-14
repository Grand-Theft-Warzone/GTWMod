package com.grandtheftwarzone.gtwmod.api.map.data.server;


import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.misc.AtumColor;

import java.util.List;

@Getter @Setter
public class MapData {

    private String id;

    private String imageId;
    private String attachedTo;
    private boolean allowLocalMarker;
    private List<String> permissions;

    private MapLocation topRight;
    private MapLocation downRiht;
    private MapLocation downLeft;
    private MapLocation topLeft;

    private boolean background;
    private AtumColor colorBackground;

    private boolean boardReach;
    private AtumColor colorBarderReach;

    private int minZoom;
    private int maxZoom;

    public int defaultMinZoom = 180;
    public int defaultMaxZoom = 1000;


    public MapData(String id, String imageId, String attachedTo, boolean allowLocalMarker, List<String> permission, MapLocation topRight, MapLocation downRiht, MapLocation downLeft, MapLocation topLeft, boolean background, AtumColor colorBackground, boolean boardReach, AtumColor colorBarderReach, int minZoom, int maxZoom) {
        this.id = id;
        this.imageId = imageId;
        this.attachedTo = attachedTo;
        this.allowLocalMarker = allowLocalMarker;
        this.permissions = permission;

        this.topRight = topRight;
        this.downRiht = downRiht;
        this.downLeft = downLeft;
        this.topLeft = topLeft;

        this.background = background;
        this.colorBackground = colorBackground;
        this.boardReach = boardReach;
        this.colorBarderReach = colorBarderReach;

        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    public MapData(String id, String imageId, String attachedTo, boolean allowLocalMarker, List<String> permission, MapLocation topRight, MapLocation downRiht, MapLocation downLeft, MapLocation topLeft, boolean background, boolean boardReach, AtumColor colorBarderReach, int minZoom, int maxZoom) {
        this(id, imageId, attachedTo, allowLocalMarker, permission, topRight, downRiht, downLeft, topLeft, background, null, boardReach, colorBarderReach, minZoom, maxZoom);
    }

    public MapData(String id, String imageId, String attachedTo, boolean allowLocalMarker, List<String> permission, MapLocation topRight, MapLocation downRiht, MapLocation downLeft, MapLocation topLeft, boolean background, boolean boardReach, int minZoom, int maxZoom) {
        this(id, imageId, attachedTo, allowLocalMarker, permission, topRight, downRiht, downLeft, topLeft, background, null, boardReach, null, minZoom, maxZoom);
    }

    public MapData(String id, String imageId, String attachedTo, boolean allowLocalMarker, List<String> permission, MapLocation topRight, MapLocation downRiht, MapLocation downLeft, MapLocation topLeft, boolean background, AtumColor colorBackground, boolean boardReach, int minZoom, int maxZoom) {
        this(id, imageId, attachedTo, allowLocalMarker, permission, topRight, downRiht, downLeft, topLeft, background, colorBackground, boardReach, null, minZoom, maxZoom);
    }

    public MapImageData toMapImageData() {
        AtumColor colorBackgroundP,  colorBoardReachP;
        if (!background) {
            colorBackgroundP = null;
        } else {
            colorBackgroundP = colorBackground;
        }
        if (!boardReach) {
            colorBoardReachP = null;
        } else {
            colorBoardReachP = colorBarderReach;
        }
        return new MapImageData(imageId, topRight, downRiht, downLeft, topLeft, colorBackgroundP, colorBoardReachP);
    }



}
