package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import net.minecraft.util.ResourceLocation;

public class ProcessConsumer {

    public ProcessConsumer() {
        blablabla();
    }

    public void blablabla() {
        GtwAPI.getInstance().getMapManagerClient().getMapConsumers().setCStartData(
                (it) ->{
                    System.out.println("МЫ ПОЛУЧИЛИ ДАННЫЕ ОТ СЕРВЕРА {StartData}! ...");

                    // @TODO У нас ещё есть ограничение - разрешены ли локальные маркеры. Реализовать позже.

                    // working with maps.
                    ResourceLocation temporaryImage = new ResourceLocation("gtwmod", "textures/gui/minimap/test_map.png");
                    ResourceLocation radarImage = new ResourceLocation("gtwmod", "textures/gui/minimap/radar.png");

                    if (it.getMinimapData() == null) {
                        System.out.println("minimap null");
                    }
                    if (it.getGlobalmapData() == null) {
                        System.out.println("globalmap null");
                    }
                    if (it.getRestrictionsData() == null) {
                        System.out.println("ограничения null");
                    }

                    if (it.getMinimapData() != null) {
                        MapImageData minimapData = it.getMinimapData();
                        MapImage minimapImage = new MapImage(temporaryImage, minimapData.getTopRight(), minimapData.getDownRight(), minimapData.getDownLeft(), minimapData.getTopLeft());
                        System.out.println("Is Allow Map Display: " + it.getRestrictionsData().isAllowMapDisplay());
                        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().updateData(minimapImage, radarImage, it.getRestrictionsData().isAllowMapDisplay());
                    }

                    if (it.getGlobalmapData() != null) {
                        // @TODO Добавить функционал глобальной карты.
                    }

                    GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setAllowedToDisplay(it.getRestrictionsData().isAllowMapDisplay(), true);
                    GtwAPI.getInstance().getMapManagerClient().getMinimapManager().updateZoomLimits(it.getRestrictionsData().getMinZoom(), it.getRestrictionsData().getMaxZoom());


                }
        );
    }

}
