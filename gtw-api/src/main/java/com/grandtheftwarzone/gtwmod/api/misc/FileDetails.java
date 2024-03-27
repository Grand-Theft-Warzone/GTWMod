package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
@Getter
@Setter
public class FileDetails {
    private String fileHash;
    private ResourceLocation resourceLocation;

    public String toString() {
        return "FileHash: " + fileHash + " : " + "RL: " + resourceLocation.toString();
    }
}
