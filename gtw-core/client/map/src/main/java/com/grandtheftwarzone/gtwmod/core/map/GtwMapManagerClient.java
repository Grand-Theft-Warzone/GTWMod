package com.grandtheftwarzone.gtwmod.core.map;


import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.event.ClientConnectEvent;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.MapImageUtils;
import com.grandtheftwarzone.gtwmod.api.map.MapManagerClient;
import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersClient;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.GtwGlobalmapManager;
import com.grandtheftwarzone.gtwmod.core.map.minimap.GtwMinimapManager;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;


public class GtwMapManagerClient implements AtumModService, MapManagerClient {


    @Getter
    private GtwMinimapManager minimapManager;

    @Getter
    private GtwGlobalmapManager globalmapManager;

    @Getter
    private MapConsumersClient mapConsumers;

    @Getter
    private ProcessConsumer processConsumer;


    public GtwMapManagerClient(AtumMod atumMod) {
        atumMod.provideModService(this);
        this.minimapManager = new GtwMinimapManager();
        this.globalmapManager = new GtwGlobalmapManager();
        this.mapConsumers = new MapConsumersClient();
    }



    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLPreInitializationEvent){
            minimapManager.onPreInit((FMLPreInitializationEvent) fmlEvent);
            this.processConsumer = new ProcessConsumer();

        }
    }


//    public void displayMiniMap() {
//
//
//        MapLocation N1 = new MapLocation(-464, -1952);
//        MapLocation N2 = new MapLocation(-464, 159);
//        MapLocation N3 = new MapLocation(1887, 159);
//        MapLocation N4 = new MapLocation(1887, -1952);
//
//        MapImage mapImage = new MapImage(new ResourceLocation("gtwmod", "textures/gui/minimap/general_map.png"), N4, N3, N2, N1);
//        this.minimapManager.updateData(mapImage, new ResourceLocation("gtwmod", "textures/gui/minimap/general_map.png"), true);
//
//    }

    @SubscribeEvent
    public void onConnectServer(ClientConnectEvent event) {
        System.out.println("Получил статус Connect...");

        // Отправляем запросы на данные и т.П.

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("event", "getStartData");
        Config config = GtwAPI.getInstance().getGtwMod().getConfigManager().createConfig(hashMap, ConfigType.JSON);
        GtwAPI.getInstance().getNetworkAPI().sendSRequest(config);

        // -------------------

//        displayMiniMap();


    }

    @SubscribeEvent
    public void onPlayerChat(ClientChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("bb")) {

        System.out.println("БАЛАБОЛ ВЫПОЛНИЛСЯ ЧАТЭВЕНТ");
//            GtwAPI.getInstance().getNetworkAPI().sendTestServer("Балабол");
//            GtwAPI.getInstance().getNetworkAPI().sendNotification(new NotificationRequest("hi", 123), UUID.fromString("120abf41-c686-3a55-8362-5f06e763dbbf"));

//            MapImageUtils.getMapImage("test", null, null);

        }
    }


    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "map";
    }

}
