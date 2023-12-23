package com.grandtheftwarzone.gtwmod.core.misc;

import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GtwSoundsManager implements SoundsManager {
    private HashMap<String, SoundEvent> sounds = new HashMap<>();

    public GtwSoundsManager(){
        registerSound("levelup");
    }

    @Override
    public void registerSound(@NotNull String id) {
        ResourceLocation location = new ResourceLocation("gtwclient",id);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        sounds.put(id, event);
    }

    @Override
    public @NotNull SoundEvent getSound(@NotNull String id) {
        return sounds.get(id);
    }

    @Override
    public @NotNull SoundEvent[] getAllRegisteredSounds() {
        return sounds.values().toArray(new SoundEvent[0]);
    }
}
