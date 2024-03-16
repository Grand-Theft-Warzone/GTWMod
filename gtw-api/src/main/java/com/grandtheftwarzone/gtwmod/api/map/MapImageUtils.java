package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.resources.BufferTextureResource;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MapImageUtils {

    private static ResourceLocation nullImage = new ResourceLocation("gtwmod", "textures/gui/minimap/nullable.png");

    @Getter
    private static File gameDir = new File("gtwdata/map/");


    public static void init(ResourceLocation folder) {
        System.out.println("Типо скапировали всё содержимое из " + folder.toString());
    }


    public static ResourceLocation getImage(String id) {
        File file = new File(gameDir, id + ".png");
        if (!file.exists()) {
            GtwLog.getLogger().error("[getImage] File " + file.getAbsolutePath() + " Not found.");
            return nullImage;
        }
        try {
            BufferTextureResource bufferTextureResource = new BufferTextureResource(file);
            bufferTextureResource.loadTexture();
            return bufferTextureResource.getResourceLocation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable ResourceLocation getMapImage(String id, @Nullable AtumColor colorBackground) {
        File file = new File(gameDir, "maps/" + id + ".png");
        if (!file.exists()) {
            GtwLog.getLogger().error("[getMapImage] File " + file.getAbsolutePath() + " Not found.");
            return nullImage;
        }



        if (colorBackground == null) {
            return getImage("maps/" + id);
        }
        try {
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

            File modifiedFile = new File(gameDir, "/maps/" + id +"_modif.png");
            if (!modifiedFile.exists()) {
                System.out.println("DELITE");
            }

            modifiedFile.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(modifiedFile);
            ImageIO.write(imageFirst, "PNG", outputStream);
            outputStream.close();

            BufferTextureResource bufImage = new BufferTextureResource(modifiedFile);
            bufImage.loadTexture();
            return bufImage.getResourceLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nullImage;
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
