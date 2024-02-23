package com.grandtheftwarzone.gtwmod.core.map;


import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.MapManager;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.GtwGlobalmapManager;
import com.grandtheftwarzone.gtwmod.core.map.minimap.GtwMinimapManager;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.jetbrains.annotations.NotNull;


public class GtwMapManager implements AtumModService, MapManager {


    @Getter
    private GtwMinimapManager minimapManager;

    @Getter
    private GtwGlobalmapManager globalmapManager;



    public GtwMapManager(AtumMod atumMod) {
        atumMod.provideModService(this);
        this.minimapManager = new GtwMinimapManager();
        this.globalmapManager = new GtwGlobalmapManager();
    }



    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLPreInitializationEvent){
            minimapManager.onPreInit((FMLPreInitializationEvent) fmlEvent);
        }
    }


    public void displayMiniMap() {


        MapLocation N1 = new MapLocation(-464, -1952);
        MapLocation N2 = new MapLocation(-464, 159);
        MapLocation N3 = new MapLocation(1887, 159);
        MapLocation N4 = new MapLocation(1887, -1952);

        MapImage mapImage = new MapImage(new ResourceLocation("gtwmod", "textures/gui/minimap/test_map.png"), N4, N3, N2, N1);
        this.minimapManager.updateData(mapImage, new ResourceLocation("gtwmod", "textures/gui/minimap/radar.png"), true);

    }

    @SubscribeEvent
    public void onPlayerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        System.out.println("Уры! Словлен эвент захода на сервер.");

        // Отправляем запросы на данные и т.П.
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "map";
    }
}
