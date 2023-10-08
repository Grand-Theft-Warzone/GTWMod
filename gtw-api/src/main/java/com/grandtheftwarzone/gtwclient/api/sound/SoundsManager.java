package com.grandtheftwarzone.gtwclient.api.sound;

import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;

public interface SoundsManager {

    /**
     * Register the sound
     *
     * @param id the id of the sound
     */
    void registerSound(@NotNull String id);

    /**
     * Get the sound
     *
     * @param id the id of the sound which will be used to get it from resources
     * @return the sound
     */
    @NotNull
    SoundEvent getSound(@NotNull String id);

    /**
     * Get all registered sounds
     *
     * @return all registered sounds
     */
    @NotNull
    SoundEvent[] getAllRegisteredSounds();

}
