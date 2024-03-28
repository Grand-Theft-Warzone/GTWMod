package com.grandtheftwarzone.gtwmod.api.misc;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {

    @Getter
    private Map<File, FileDetails> fileMap = new HashMap<>();

    public FileLoader(File folderPath) {
        // Проверяем, является ли путь папкой
        if (!folderPath.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a directory.");
        }

        // Рекурсивно обрабатываем все файлы и подпапки
        updateDirImage(folderPath);


    }

    public void updateDirImage(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("Обнаружил " + file.toString());
                    if (!(file.toString().endsWith(".png") || file.toString().endsWith(".jpeg"))) {
                        GtwLog.getLogger().warn("File " + file + " is not .png || .jpeg");
                        continue;
                    }
                    try {
                        ResourceLocation resourceLocationFile = FileImageUtils.getRLImagefromFile(file);
                        if (resourceLocationFile == null) {
                            GtwLog.getLogger().error("ResourceLocation " + file + " received null");
                            continue;
                        }
                        FileDetails fileDetails = new FileDetails(calculateFileHash(file), resourceLocationFile);
                        System.out.println("BLABLABLA " + fileDetails.toString());
                        fileMap.put(file, fileDetails);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (file.isDirectory()) {
                    // Рекурсивно обрабатываем подпапку
                    updateDirImage(file);
                }
            }
        }

    }

    public void updateFileImage(File file) {
        if (!(file.toString().endsWith(".png") || file.toString().endsWith(".jpeg"))) {
            GtwLog.getLogger().warn("File " + file + " is not .png || .jpeg");
            return;
        }
        try {
            ResourceLocation resourceLocationFile = FileImageUtils.getRLImagefromFile(file);
            if (resourceLocationFile == null) {
                GtwLog.getLogger().error("ResourceLocation " + file + " received null");
                return;
            }
            FileDetails fileDetails = new FileDetails(calculateFileHash(file), resourceLocationFile);
            fileMap.put(file, fileDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateFile(File file, ResourceLocation resourceLocation) {
        try {
            FileDetails fileDetails = new FileDetails(calculateFileHash(file), resourceLocation);
            fileMap.put(file, fileDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable ResourceLocation getResourceLocationOrNull(File file) throws IOException {
        if (file.isDirectory() || !file.exists()) {
            GtwLog.getLogger().error("[getResourceLocationOrNull] File "+file+" not found! (Or is a folder)");
            fileMap.remove(file);
            return null;
        }

        FileDetails fileDetails = fileMap.get(file);
        if (fileDetails == null) {
            GtwLog.getLogger().warn("[getResourceLocationOrNull] FileDetails " + " файла " + file + "= null");
            fileMap.remove(file);
            return null;
        }

        if (!calculateFileHash(file).equals(fileDetails.getFileHash())) {
            GtwLog.getLogger().warn("[getResourceLocationOrNull] The file " + file + " has been modified!");
            fileMap.remove(file);
            return null;
        }

        if (fileDetails.getResourceLocation() == null) {
            GtwLog.getLogger().error("FileDetails " + fileDetails + " RL is null");
            fileMap.remove(file);
            return null;
        }

        return fileDetails.getResourceLocation();

    }

    public void removeFile(File file) {
        fileMap.remove(file);
    }

    public static String calculateFileHash(File file) throws IOException {
        return DigestUtils.sha256Hex(FileUtils.readFileToByteArray(file));
    }

}

