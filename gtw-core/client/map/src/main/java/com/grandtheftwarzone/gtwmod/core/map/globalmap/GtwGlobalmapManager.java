package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.GlobalmapManager;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import com.grandtheftwarzone.gtwmod.api.map.data.client.UpdateMinimapData;
import com.grandtheftwarzone.gtwmod.api.map.data.server.UpdateGlobalmapData;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class GtwGlobalmapManager implements GlobalmapManager {

    private static KeyBinding keyShowMap;
//    private boolean globalmapActive = false;
    @Getter
    private MapImage globalmapImage;
    private ResourceLocation globalmapTexture;
    @Getter
    @Setter
    private UpdateGlobalmapData updatingData = null;

    @Getter @Setter
    private boolean initCanvasDraw = false;


    private final Minecraft mc = Minecraft.getMinecraft();

    public GtwGlobalmapManager() {

        EVENT_BUS.register(this);

    }


    public void updateData(MapImageData globalmapData, Boolean draw) {

        System.out.println("[globalmap] updateData");

        ResourceLocation mapTexture = GtwAPI.getInstance().getMapManagerClient().getMapImageUtils().getMapImage(globalmapData.getImageId(), globalmapData.getColorBackground());
        this.globalmapImage = new MapImage(mapTexture, globalmapData.getImageId(), globalmapData.getTopRight(), globalmapData.getDownRight(), globalmapData.getDownLeft(), globalmapData.getTopLeft(), globalmapData.getOffsetX(), globalmapData.getOffsetY());
        this.globalmapTexture = mapTexture;

        this.initCanvasDraw = false;
        if (draw == null) {
            draw = GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay();
        }
        GtwAPI.getInstance().getMapManagerClient().setAllowedToDisplay(draw, true);
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event) {
        if (updatingData != null) {
            if (updatingData.getAllowDisplay() != null) {
                updateData(updatingData.getMapImageData(), updatingData.getAllowDisplay());
            } else {
                updateData(updatingData.getMapImageData(), null);
            }

            this.updatingData = null;
        }
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        keyShowMap = new KeyBinding("key.globalmap.show.desc", Keyboard.KEY_M, "key.categories.mod");

        ClientRegistry.registerKeyBinding(keyShowMap);

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) return;
        if (keyShowMap.isPressed()) {

            // @TODO Добавить условия, тобишь проверку, разрешено ли отображение. (Ну и др.)
            System.out.println("Отследил нажатие на M");
            System.out.println("Пытаюсь открыть карту");
            Minecraft.getMinecraft().displayGuiScreen(
                    new GtwGlobalmapScreen(GtwAPI.getInstance().getGtwMod(), "globalmap")
            );
        }

    }

}
