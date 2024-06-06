package com.grandtheftwarzone.gtwmod.core.map;


import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.event.ClientConnectEvent;
import com.grandtheftwarzone.gtwmod.api.map.MapImageUtils;
import com.grandtheftwarzone.gtwmod.api.map.manager.client.MapManagerClient;
import com.grandtheftwarzone.gtwmod.api.map.consumer.MapConsumersClient;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.GtwGlobalmapManager;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.RadarClient;
import com.grandtheftwarzone.gtwmod.core.map.minimap.GtwMinimapManager;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.HashMap;


public class GtwMapManagerClient implements AtumModService, MapManagerClient {

    @Getter
    private KeyBinding keyShowGlobalmap;

    @Getter
    private KeyBinding keyShowMinimap;

    @Getter
    private KeyBinding keyIncreaseZoom;

    @Getter
    private KeyBinding keyDecreaseZoom;

    @Getter
    private GtwMinimapManager minimapManager;

    @Getter
    private GtwGlobalmapManager globalmapManager;

    @Getter
    private GtwMarkerManagerClient markerManager;

    @Getter
    private MapConsumersClient mapConsumers;

    @Getter
    private ProcessConsumer processConsumer;

    @Getter
    private boolean allowedToDisplay = false;

    @Getter
    private MapImageUtils mapImageUtils;

    //

    @Getter @Setter
    private RadarClient radarPlayer;


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
            globalmapManager.onPreInit((FMLPreInitializationEvent) fmlEvent);
            this.processConsumer = new ProcessConsumer();
            this.onPreInit((FMLPreInitializationEvent) fmlEvent);
        }else if (fmlEvent instanceof FMLPostInitializationEvent) {
            onPostInit((FMLPostInitializationEvent) fmlEvent);
        }else if(fmlEvent instanceof FMLInitializationEvent){
            onInit((FMLInitializationEvent) fmlEvent);
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
//        MapImage mapImage = new MapImage(new ResourceLocation("gtwmod", "textures/gui/map/general_map_hd.png"), N4, N3, N2, N1);
//        this.minimapManager.updateData(mapImage, new ResourceLocation("gtwmod", "textures/gui/map/general_map_hd.png"), true);
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

    // ДЕБАГ. УДАЛИТЬ. @TODO Удали!
    @SubscribeEvent
    public void onPlayerChat(ClientChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("bb")) {

            System.out.println("БАЛАБОЛ ВЫПОЛНИЛСЯ ЧАТЭВЕНТ");
//            GtwAPI.getInstance().getNetworkAPI().sendTestServer("Балабол");
//            GtwAPI.getInstance().getNetworkAPI().sendNotification(new NotificationRequest("hi", 123), UUID.fromString("120abf41-c686-3a55-8362-5f06e763dbbf"));

//            MapImageUtils.getMapImage("test", null, null);

        }



    }


    /**
     * Изменяет переменную allowedToDisplay и статус отображения.
     * @param draw значение переменной.
     * @param quietChange изменять ли значение переменной после изменения значения?
     */
    public void setAllowedToDisplay(Boolean draw, Boolean quietChange) {
        System.out.println(draw);
        if (draw == null) {System.out.println("Draw is null");}
        DisplayRenderer render = GtwAPI.getInstance().getGtwMod().getDisplayManager().getHUDCanvas()
                .getDisplayRenderer();
        if(render==null) return;
        if (quietChange) {
            render.getDisplayData().setElementEnabled("minimap", draw);
        }
        this.allowedToDisplay = draw;
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        keyShowMinimap = new KeyBinding("key.minimap.show.desc", Keyboard.KEY_U, "key.categories.mod");
        keyIncreaseZoom = new KeyBinding("key.minimap.increase.desc", Keyboard.KEY_PRIOR, "key.categories.mod");
        keyDecreaseZoom = new KeyBinding("key.minimap.decrease.desc", Keyboard.KEY_NEXT, "key.categories.mod");
        keyShowGlobalmap = new KeyBinding("key.globalmap.show.desc", Keyboard.KEY_M, "key.categories.mod");

        ClientRegistry.registerKeyBinding(keyIncreaseZoom);
        ClientRegistry.registerKeyBinding(keyDecreaseZoom);
        ClientRegistry.registerKeyBinding(keyShowMinimap);
        ClientRegistry.registerKeyBinding(keyShowGlobalmap);

    }

    public void onInit(FMLInitializationEvent event) {
//        MinecraftForge.EVENT_BUS.register(new CommandMapManager());
        MinecraftForge.EVENT_BUS.register(new CommandMapChat());
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        System.out.println("[MapManager] PostInit start");
        mapImageUtils = new MapImageUtils(new File("gtwdata/map/"));
        System.out.println("[MapManager] PostInit stop");

        this.markerManager = new GtwMarkerManagerClient();


        // Инициализация радара
        radarPlayer = new RadarClient(new EntityLocation(Minecraft.getMinecraft().player), "Radar_player", "I", null, 1, 1);
        markerManager.addLocalMarker(radarPlayer, false);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "map";
    }


}
