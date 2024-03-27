package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
@Getter
@Setter
public class FileDetails {
    private String fileHash;
    private ResourceLocation resourceLocation;
}
