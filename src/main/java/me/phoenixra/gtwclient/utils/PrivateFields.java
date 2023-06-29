package me.phoenixra.gtwclient.utils;


import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundEventAccessor;

import java.util.List;

public class PrivateFields<P, T> {
    public final Class<P> parentClass;

    private final String fieldName;

    private boolean errorReported;

    private PrivateFields(Class<P> owner, ObfuscationMapping mapping) {
        this.parentClass = owner;
        this.fieldName = mapping.getName();
    }

    public T get(P instance) {
        try {
            return Reflection.getPrivateValue(this.parentClass, instance, this.fieldName);
        }catch (Exception ex) {
            if(!this.errorReported) {
                this.errorReported = true;
                ex.printStackTrace();
            }
            return null;
        }
    }

    public T set(P instance, T value) {
        try {
            Reflection.setPrivateValue(this.parentClass, instance, this.fieldName, value);
        } catch (Exception ex) {
            if (!this.errorReported) {
                this.errorReported = true;
                ex.printStackTrace();
            }
        }
        return value;
    }


    public static final PrivateFields<SoundEventAccessor, List<ISoundEventAccessor>> eventSounds = new PrivateFields((Class)SoundEventAccessor.class, ObfuscationMapping.eventSounds);

}