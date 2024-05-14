package com.grandtheftwarzone.gtwmod.api.map.manager.client;

import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.data.server.UpdateGlobalmapData;
import com.grandtheftwarzone.gtwmod.api.map.misc.GlobalCentrCoord;
import com.grandtheftwarzone.gtwmod.api.map.misc.GlobalZoom;

public interface GlobalmapManager {

    MapImage getGlobalmapImage();
    UpdateGlobalmapData getUpdatingData();
    void setUpdatingData(UpdateGlobalmapData updatingData);

    boolean isInitCanvasDraw();
    void setInitCanvasDraw(boolean draw);

    GlobalZoom getGlobalZoom();

    GlobalCentrCoord getCentrCoord();
}
