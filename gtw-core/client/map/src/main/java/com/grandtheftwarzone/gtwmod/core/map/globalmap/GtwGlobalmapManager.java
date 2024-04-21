package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.GlobalmapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class GtwGlobalmapManager implements GlobalmapManager {

    private static KeyBinding keyShowMap;

    private boolean globalmapActive = false;

    private final Minecraft mc = Minecraft.getMinecraft();

    public GtwGlobalmapManager() {

        EVENT_BUS.register(this);

    }

    public void onPreInit(FMLPreInitializationEvent event) {
        keyShowMap = new KeyBinding("key.globalmap.show.desc", Keyboard.KEY_M, "key.categories.mod");

        ClientRegistry.registerKeyBinding(keyShowMap);

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) return;
        if (keyShowMap.isPressed()) {

            System.out.println("Отследил нажатие на M");
            System.out.println("Пытаюсь открыть карту");
            Minecraft.getMinecraft().displayGuiScreen(
                    new GtwGlobalmapScreen(GtwAPI.getInstance().getGtwMod(), "globalmap")
            );
        }

    }

}
