package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.grandtheftwarzone.gtwmod.api.map.MapImageUtils.getFileInputStream;

@Getter
public class MapImage {

    private final MapLocation topRight, downRight, downLeft, topLeft;

    private final ResourceLocation image;

    private int imageWidth, imageHeight;

    private final double pixelsPerBlockX;
    private final double pixelsPerBlockZ;


    private int widthInBlocks;
    private int heightInBlocks;


    public MapImage(ResourceLocation inputImage, String imageId, MapLocation topRight, MapLocation downRight, MapLocation downLeft, MapLocation topLeft, int offsetX, int offsetY) {
        this.image = inputImage;
        this.topRight = topRight;
        this.downRight = downRight;
        this.downLeft = downLeft;
        this.topLeft = topLeft;


        System.out.println("" +
                " Top Right: " + topRight +
                " Down Right: " + downRight +
                " Down Left" + downLeft +
                " Top Left" + topLeft
        );

        try {
            if (inputImage.getResourcePath().startsWith("dynamic")) {
                System.out.println("Запускаю первый алгоритм");
                File file = new File(MapImageUtils.getGameDir(), "/maps/" + imageId + ".png");
                InputStream stream = getFileInputStream(file);
                BufferedImage imageFirst = ImageIO.read(stream);

                this.imageWidth = imageFirst.getWidth() + offsetX;
                this.imageHeight = imageFirst.getHeight() + offsetY;

            } else {
                System.out.println("Запускаю другой алгоритм");
                InputStream imageStream = Minecraft.getMinecraft().getResourceManager().getResource(inputImage).getInputStream();

                BufferedImage image = ImageIO.read(imageStream);
                this.imageWidth = image.getWidth() + offsetX;
                this.imageHeight = image.getHeight() + offsetY;
            }
        } catch (IOException e) {
            GtwLog.getLogger().error(String.valueOf(e));
        }

        System.out.println("W: " + this.imageWidth + " H: " + this.imageHeight);

        this.pixelsPerBlockX = imageWidth / Math.abs(downRight.getX() - downLeft.getX());
        this.pixelsPerBlockZ = imageHeight / Math.abs(topLeft.getY() - downLeft.getY());

        this.widthInBlocks = (int) Math.abs(downRight.getX() - downLeft.getX());
        this.heightInBlocks = (int) Math.abs(downRight.getY() - topRight.getY());
    }

    public MapLocation calculateCoord(double targetX, double targetY) {
        double targetRelativeX = targetX - topLeft.getX();
        double targetRelativeZ = targetY - topLeft.getY();
        int mapX = (int) (targetRelativeX * this.pixelsPerBlockX);
        int mapZ = (int) (targetRelativeZ * this.pixelsPerBlockZ);
        return new MapLocation(mapX, mapZ);

    }

    public boolean inRealMap(double targetX, double targetY) {
        return !(targetX < downLeft.getX() || targetX > downRight.getX() || targetY > downLeft.getY() || targetY < topLeft.getY());
    }


    public float proximityToTheBorder(double targetX, double targetY) {
        float prozentBorderOnX = (float) ((targetX - downLeft.getX()) / widthInBlocks);
        float prozentBorderOnY = (float) ((targetY - topLeft.getY()) / heightInBlocks);
        if (prozentBorderOnX < 0.5) {
            prozentBorderOnX = 1 - prozentBorderOnX;
        }
        if (prozentBorderOnY < 0.5) {
            prozentBorderOnY = 1 - prozentBorderOnY;
        }

        return Math.max(prozentBorderOnX, prozentBorderOnY);
    }


}
