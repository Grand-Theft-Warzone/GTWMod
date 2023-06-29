package me.phoenixra.gtwclient.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundsHandler {
    public static SoundEvent USER_LEVEL_UP;
    public static SoundEvent MAIN_MENU_THEME;

    public static void registerSounds()
    {
        USER_LEVEL_UP = registerSound("levelup");
        MAIN_MENU_THEME = registerSound("main_menu_theme");
    }

    private static SoundEvent registerSound(String name)
    {
        ResourceLocation location = new ResourceLocation("gtwclient",name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        return event;
    }

    public static SoundEvent[] getAllSounds() {
        return new SoundEvent[] {USER_LEVEL_UP, MAIN_MENU_THEME};
    }
}
