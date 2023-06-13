package me.phoenixra.playerhud.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundsHandler {
    public static SoundEvent USER_LEVEL_UP;

    public static void registerSounds()
    {
        USER_LEVEL_UP = registerSound("levelup");
    }

    private static SoundEvent registerSound(String name)
    {
        ResourceLocation location = new ResourceLocation("playerhud",name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        return event;
    }
}
