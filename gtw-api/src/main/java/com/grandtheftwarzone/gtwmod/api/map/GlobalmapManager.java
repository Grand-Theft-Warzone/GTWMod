package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.map.data.server.UpdateGlobalmapData;

public interface GlobalmapManager {

    MapImage getGlobalmapImage();
    UpdateGlobalmapData getUpdatingData();
    void setUpdatingData(UpdateGlobalmapData updatingData);

    boolean isInitCanvasDraw();
    void setInitCanvasDraw(boolean draw);


}
