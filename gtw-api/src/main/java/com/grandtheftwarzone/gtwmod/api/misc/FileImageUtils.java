package com.grandtheftwarzone.gtwmod.api.misc;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.resources.BufferTextureResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class FileImageUtils {


    public static ResourceLocation nullImage = new ResourceLocation("gtwmod", "textures/gui/minimap/nullable.png");

    @Getter
    private File rootDir;

    @Getter
    private FileLoader fileLoader = null;

    public FileImageUtils(File rootDir, boolean usageFileLoader) {
        this.rootDir = rootDir;
        if (usageFileLoader) {
            fileLoader = new FileLoader(rootDir);
        }
    }




    public ResourceLocation getImage(String idName) {

        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        String id = idName + ".png";
        File file = new File(rootDir, id);
        if (!file.exists()) {
            GtwLog.getLogger().error("[getImage] File " + file.getAbsolutePath() + " Not found.");
            if (fileLoader != null) {
                fileLoader.removeFile(file);
            }
            GtwLog.getLogger().error("[getImage] The search path in the mod resources was not transferred!");

            return nullImage;
        }

        if (fileLoader != null) {
            try {
                ResourceLocation resourceLocationImage = fileLoader.getResourceLocationOrNull(file);
                if (resourceLocationImage == null) { //That is, the file has been changed
                    resourceLocationImage = getRLImagefromFile(file);
                    fileLoader.updateFileImage(file);
                }
                return resourceLocationImage;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getRLImagefromFile(file);
        }
    }




    public ResourceLocation getImage(String idName, String localSearchDir) {

        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        String id = idName + ".png";
        File file = new File(rootDir, id);
        if (!file.exists()) {
            GtwLog.getLogger().error("[getImage] File " + file.getAbsolutePath() + " Not found.");
            if (fileLoader != null) {
                fileLoader.removeFile(file);
            }
            GtwLog.getLogger().error("[getImage] I'm looking for a mod in resources...");

            IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
            ResourceLocation resourceLocation = new ResourceLocation("gtwmod", localSearchDir + id);

            try {
                IResource resource = resourceManager.getResource(resourceLocation);
                GtwLog.getLogger().debug("[getMapImage] Image "+idName+" detected.");
                InputStream inputStream = resource.getInputStream();
                BufferedImage image = ImageIO.read(inputStream);

                file.createNewFile();

                FileOutputStream outputStream = new FileOutputStream(file);
                ImageIO.write(image, "PNG", outputStream);
                outputStream.close();

                GtwLog.getLogger().debug("[getMapImage] Image "+idName+" copied.");

                fileLoader.updateFileImage(file);

                return resourceLocation;
            } catch (IOException e) {
                // Ресурс не найден
                return nullImage;
            }
        }
        if (fileLoader != null) {
            try {
                ResourceLocation resourceLocationImage = fileLoader.getResourceLocationOrNull(file);
                if (resourceLocationImage == null) { //That is, the file has been changed
                    resourceLocationImage = getRLImagefromFile(file);
                    fileLoader.updateFileImage(file);
                }
                return resourceLocationImage;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getRLImagefromFile(file);
        }
    }

    public static @Nullable ResourceLocation getRLImagefromFile(File file) {

        if (!file.exists()) {
            GtwLog.getLogger().error("[getRLImagefromFile] File " + file.getAbsolutePath() + " Not found.");
            return null;
        }
        try {
            BufferTextureResource bufferTextureResource = new BufferTextureResource(file);
            bufferTextureResource.loadTexture();
            return bufferTextureResource.getResourceLocation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static InputStream getFileInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
