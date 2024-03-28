package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.misc.FileImageUtils;
import com.grandtheftwarzone.gtwmod.api.misc.FileLoader;
import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.resources.BufferTextureResource;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MapImageUtils extends FileImageUtils {


    @Getter
    private File gameDir;


    public MapImageUtils(File gameDir) {
        super(gameDir, true);
        this.gameDir = gameDir;
    }

    @Override
    public ResourceLocation getImage(String idName) {
        return super.getImage(idName, "textures/gui/minimap/");
    }

    public @Nullable ResourceLocation getMapImage(String id, @Nullable AtumColor colorBackground) {

        System.out.println("F1");

        File file = new File(gameDir, "maps/" + id + ".png");


        File maps = new File(gameDir, "maps");
        if (!maps.exists()) {
            maps.mkdir();
        }

        if (!file.exists()) {
            GtwLog.getLogger().error("[getMapImage] File " + file.getAbsolutePath() + " Not found.");
            GtwLog.getLogger().debug("[getMapImage] I'm looking for a mod in resources...");

            IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
            ResourceLocation resourceLocation = new ResourceLocation("gtwmod", "textures/gui/minimap/maps/" + id + ".png");

            try {
                IResource resource = resourceManager.getResource(resourceLocation);
                GtwLog.getLogger().debug("[getMapImage] Image "+id+" detected.");
                InputStream inputStream = resource.getInputStream();
                BufferedImage image = ImageIO.read(inputStream);

                file.createNewFile();

                FileOutputStream outputStream = new FileOutputStream(file);
                ImageIO.write(image, "PNG", outputStream);
                outputStream.close();

                GtwLog.getLogger().debug("[getMapImage] Image "+id+" copied.");


            } catch (IOException e) {
                // Ресурс не найден
                return nullImage;
            }
        }
        System.out.println("F2");



        if (colorBackground == null) {
            return super.getImage("maps/" + id, "textures/gui/minimap/");
        }

        try {
            System.out.println("F3");
            InputStream stream = getFileInputStream(file);
            BufferedImage imageFirst = ImageIO.read(stream);

            int width = imageFirst.getWidth();
            int height = imageFirst.getHeight();

//            if (colorBackground != null) {
//                BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//                Graphics2D g2d = background.createGraphics();
//                g2d.setColor(new Color(colorBackground.toInt(), true));
//                g2d.fillRect(0, 0, width, height);
//                g2d.dispose();
//
//                Graphics2D g2dImage = background.createGraphics();
//                g2dImage.drawImage(imageFirst, 0, 0, null);
//                g2dImage.dispose();
//                imageFirst = background;
//            }

            System.out.println("F for for");
            if (colorBackground != null) {
                for (int y = 0; y < height; y++) {
                    imageFirst.setRGB(0, y, colorBackground.toInt()); // Левый край
                    imageFirst.setRGB(width - 1, y, colorBackground.toInt()); // Правый край
                }
                for (int x = 0; x < width; x++) {
                    imageFirst.setRGB(x, 0, colorBackground.toInt()); // Верхний край
                    imageFirst.setRGB(x, height - 1, colorBackground.toInt()); // Нижний край
                }
            }
            System.out.println("F for for end");
            File modifiedFile = new File(gameDir, "/maps/" + id +"_modif.png");

            if (!modifiedFile.exists()) {
                System.out.println("DELITE");
            }

            modifiedFile.createNewFile();
            System.out.println("F create end");

            FileOutputStream outputStream = new FileOutputStream(modifiedFile);
            ImageIO.write(imageFirst, "PNG", outputStream);
            outputStream.close();
            System.out.println("F4");

            if (super.getFileLoader() != null) {

                ResourceLocation resourceLocationImage = super.getFileLoader().getResourceLocationOrNull(modifiedFile);
                if (resourceLocationImage == null) {
                    resourceLocationImage = FileImageUtils.getRLImagefromFile(modifiedFile);
                    super.getFileLoader().updateFileImage(modifiedFile);
                }
                System.out.println("F5");
                return resourceLocationImage;

            } else {
                System.out.println("F6");
                return FileImageUtils.getRLImagefromFile(modifiedFile);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return nullImage;
    }


}
