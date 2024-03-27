package com.grandtheftwarzone.gtwmod.api.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {

    @Getter
    private Map<String, Map<String, FileDetails>> fileMap = new HashMap<>();

    public FileLoader(File folderPath) {

    }

}

