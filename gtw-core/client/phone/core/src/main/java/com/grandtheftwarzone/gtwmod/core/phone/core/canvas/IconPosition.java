package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import java.util.ArrayList;
import java.util.List;

public class IconPosition {
    int x, y, size;

    protected IconPosition(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }


    public static List<IconPosition> calculateIconLayout(int xOrigin,
                                                         int yOrigin,
                                                         int displayWidth,
                                                         int displayHeight,
                                                         int iconsAmount,
                                                         int iconsPerRow,
                                                         int padding) {
        List<IconPosition> positions = new ArrayList<>();
        int rows = (int) Math.ceil((double) iconsAmount / iconsPerRow);

        // Calculate the available space for icons, subtracting the padding
        int availableWidth = displayWidth - (padding * (iconsPerRow + 1));
        int availableHeight = displayHeight - (padding * (rows + 1));

        int iconSize = Math.min(availableWidth / iconsPerRow, availableHeight / rows);

        for (int i = 0; i < iconsAmount; i++) {
            int row = i / iconsPerRow;
            int col = i % iconsPerRow;

            // Calculate position with padding included
            int x = (col * iconSize) + (padding * (col + 1));
            int y = (row * iconSize) + (padding * (row + 1));

            positions.add(new IconPosition(x+xOrigin, y+yOrigin, iconSize));
        }
        return positions;
    }

    @Override
    public String toString() {
        return "Position: (" + x + ", " + y + "), Size: " + size;
    }

}
