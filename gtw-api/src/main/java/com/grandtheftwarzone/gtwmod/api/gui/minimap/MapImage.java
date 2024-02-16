package com.grandtheftwarzone.gtwmod.api.gui.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.misc.MapCord;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class MapImage {

    private final MapCord topLeft, downLeft, downRight, topRight;

    private final ResourceLocation image;

    private int imageWidth, imageHeight;

    private final double pixelsPerBlockX;
    private final double pixelsPerBlockZ;


    private int widthInBlocks;
    private int heightInBlocks;

    public MapImage(ResourceLocation inputImage, MapCord topLeft, MapCord downLeft, MapCord downRight, MapCord topRight) {
        this.image = inputImage;
        this.topLeft = topLeft;
        this.downLeft = downLeft;
        this.downRight = downRight;
        this.topRight = topRight;

        try {
            InputStream imageStream = Minecraft.getMinecraft().getResourceManager().getResource(inputImage).getInputStream();
            BufferedImage image = ImageIO.read(imageStream);
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
        } catch (IOException e) {
            GtwLog.error(String.valueOf(e));
        }

        this.pixelsPerBlockX = imageWidth / Math.abs(downRight.getX() - downLeft.getX());
        this.pixelsPerBlockZ = imageHeight / Math.abs(topLeft.getY() - downLeft.getY());

        this.widthInBlocks = (int) Math.abs(downRight.getX() - downLeft.getX());
        this.heightInBlocks = (int) Math.abs(downRight.getY() - topRight.getY());
    }

    public MapImage(ResourceLocation inputImage, int imageWidth, int imageHeight, MapCord topLeft, MapCord downLeft, MapCord downRight, MapCord topRight) {
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

    public MapCord calculateCoord(double targetX, double targetY) {
        double targetRelativeX = targetX - topLeft.getX();
        double targetRelativeZ = targetY - topLeft.getY();
        int mapX = (int) (targetRelativeX * this.pixelsPerBlockX);
        int mapZ = (int) (targetRelativeZ * this.pixelsPerBlockZ);
        return new MapCord(mapX, mapZ);

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

        System.out.println("Близость: " + Math.max(prozentBorderOnX, prozentBorderOnY));
        return Math.max(prozentBorderOnX, prozentBorderOnY);
    }


}