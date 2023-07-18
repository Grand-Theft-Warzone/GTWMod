package me.phoenixra.gtwclient.api.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class GuiElementColor {
    public static final GuiElementColor WHITE = new GuiElementColor(1,1,1);
    public static final GuiElementColor BLACK = new GuiElementColor(0,0,0);
    public static final GuiElementColor RED = new GuiElementColor(1,0,0);
    public static final GuiElementColor GREEN = new GuiElementColor(0,1,0);
    public static final GuiElementColor BLUE = new GuiElementColor(0,0,1);
    public static final GuiElementColor YELLOW = new GuiElementColor(1,1,0);
    public static final GuiElementColor CYAN = new GuiElementColor(0,1,1);
    public static final GuiElementColor MAGENTA = new GuiElementColor(1,0,1);
    public static final GuiElementColor GRAY = new GuiElementColor(0.5f,0.5f,0.5f);
    public static final GuiElementColor DARK_GRAY = new GuiElementColor(0.25f,0.25f,0.25f);
    public static final GuiElementColor LIGHT_GRAY = new GuiElementColor(0.75f,0.75f,0.75f);
    public static final GuiElementColor ORANGE = new GuiElementColor(1,0.5f,0);
    public static final GuiElementColor PINK = new GuiElementColor(1,0.68f,0.68f);
    public static final GuiElementColor PURPLE = new GuiElementColor(0.5f,0,0.5f);
    public static final GuiElementColor BROWN = new GuiElementColor(0.5f,0.25f,0);

    @Getter
    private float red;
    @Getter
    private float green;
    @Getter
    private float blue;


    public int toInt() {
        return ((int) (red * 255) << 16) | ((int) (green * 255) << 8) | (int) (blue * 255);
    }

    public static GuiElementColor from(float red, float green, float blue) {
        return new GuiElementColor(
                red / 255,
                green / 255,
                blue / 255
        );
    }
    //from int
    public static GuiElementColor from(int color) {
        return new GuiElementColor(
                (color >> 16) & 0xFF,
                (color >> 8) & 0xFF,
                color & 0xFF
        );
    }

}
