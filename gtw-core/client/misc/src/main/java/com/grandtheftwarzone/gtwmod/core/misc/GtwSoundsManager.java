package com.grandtheftwarzone.gtwmod.core.misc;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import lombok.Getter;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GtwSoundsManager implements SoundsManager, AtumModService {
    @Getter
    private final String id = "sounds";
    private HashMap<String, SoundEvent> sounds = new HashMap<>();

    public GtwSoundsManager(){
        GtwAPI.getInstance().getGtwMod().provideModService(this);
    }
    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLInitializationEvent){
            registerSound("levelup");
        }
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void registerSound(@NotNull String id) {
        ResourceLocation location = new ResourceLocation("gtwmod",id);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        ForgeRegistries.SOUND_EVENTS.register(event);
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
