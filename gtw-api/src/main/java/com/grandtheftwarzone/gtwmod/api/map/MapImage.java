package com.grandtheftwarzone.gtwmod.api.map;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class MapImage {

    private final MapLocation topRight, downRight, downLeft, topLeft;

    private final ResourceLocation image;

    private int imageWidth, imageHeight;

    private final double pixelsPerBlockX;
    private final double pixelsPerBlockZ;


    private int widthInBlocks;
    private int heightInBlocks;

    public MapImage(ResourceLocation inputImage, MapLocation topRight, MapLocation downRight, MapLocation downLeft, MapLocation topLeft) {
        this.image = inputImage;
        this.topRight = topRight;
        this.downRight = downRight;
        this.downLeft = downLeft;
        this.topLeft = topLeft;

        try {
            InputStream imageStream = Minecraft.getMinecraft().getResourceManager().getResource(inputImage).getInputStream();
            BufferedImage image = ImageIO.read(imageStream);
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
        } catch (IOException e) {
            GtwLog.getLogger().error(String.valueOf(e));
        }

        this.pixelsPerBlockX = imageWidth / Math.abs(downRight.getX() - downLeft.getX());
        this.pixelsPerBlockZ = imageHeight / Math.abs(topLeft.getY() - downLeft.getY());

        this.widthInBlocks = (int) Math.abs(downRight.getX() - downLeft.getX());
        this.heightInBlocks = (int) Math.abs(downRight.getY() - topRight.getY());
    }

    public MapImage(ResourceLocation inputImage, int imageWidth, int imageHeight, MapLocation topLeft, MapLocation downLeft, MapLocation downRight, MapLocation topRight) {
        this.image = inputImage;
        this.topLeft = topLeft;
        this.downLeft = downLeft;
        this.downRight = downRight;
        this.topRight = topRight;

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        this.pixelsPerBlockX = imageWidth / (downRight.getX() - downLeft.getX());
        this.pixelsPerBlockZ = imageHeight / (topLeft.getY() - downLeft.getY());

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
