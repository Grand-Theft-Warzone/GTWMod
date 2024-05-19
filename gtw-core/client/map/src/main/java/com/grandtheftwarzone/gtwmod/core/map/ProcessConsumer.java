package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import com.grandtheftwarzone.gtwmod.api.map.data.client.UpdateMinimapData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.UpdateGlobalmapData;
import me.phoenixra.atumodcore.api.display.misc.resources.BufferTextureResource;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.IOException;


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
//                    ResourceLocation temporaryImage = new ResourceLocation("gtwmod", "textures/gui/map/general_map_hd.png");
//                    ResourceLocation radarImage = new ResourceLocation("gtwmod", "textures/gui/map/general_map_hd.png");

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
                        System.out.println("Is Allow Map Display: " + it.getRestrictionsData().isAllowMapDisplay());
                        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setUpdatingData(new UpdateMinimapData(it.getMinimapData(), "radar", it.getRestrictionsData().isAllowMapDisplay()));
//                        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().updateData(it.getMinimapData(), "radar", it.getRestrictionsData().isAllowMapDisplay());
                    }

                    if (it.getGlobalmapData() != null) {
                        // @TODO Добавить функционал глобальной карты.
                        GtwAPI.getInstance().getMapManagerClient().getGlobalmapManager().setUpdatingData(new UpdateGlobalmapData(it.getGlobalmapData(), it.getRestrictionsData().isAllowMapDisplay()));
                    }

                    GtwAPI.getInstance().getMapManagerClient().setAllowedToDisplay(it.getRestrictionsData().isAllowMapDisplay(), true);
                    GtwAPI.getInstance().getMapManagerClient().getMinimapManager().updateZoomLimits(it.getRestrictionsData().getMinZoom(), it.getRestrictionsData().getMaxZoom());


                }
        );

        System.out.print("HHHHH");
        GtwAPI.getInstance().getMapManagerClient().getMapConsumers().setCMarkersList(
                (it) -> {
                    System.out.println("МЫ ПОЛУЧИЛИ ДАННЫЕ ОТ СЕРВЕРА {Markerms}! ...");

//                    GtwAPI.getInstance().getMapManagerClient().getMarkerManager().setServerMarkerList(it);
                }

        );
    }

}