package me.phoenixra.gtwclient.api.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;

@AllArgsConstructor
public class GuiElementColor {
    public static final GuiElementColor WHITE = new GuiElementColor(1,1,1,1);
    public static final GuiElementColor BLACK = new GuiElementColor(0,0,0,1);
    public static final GuiElementColor RED = new GuiElementColor(1,0,0,1);
    public static final GuiElementColor GREEN = new GuiElementColor(0,1,0,1);
    public static final GuiElementColor BLUE = new GuiElementColor(0,0,1,1);
    public static final GuiElementColor YELLOW = new GuiElementColor(1,1,0,1);
    public static final GuiElementColor CYAN = new GuiElementColor(0,1,1,1);
    public static final GuiElementColor MAGENTA = new GuiElementColor(1,0,1,1);
    public static final GuiElementColor GRAY = new GuiElementColor(0.5f,0.5f,0.5f,1);
    public static final GuiElementColor DARK_GRAY = new GuiElementColor(0.25f,0.25f,0.25f,1);
    public static final GuiElementColor LIGHT_GRAY = new GuiElementColor(0.75f,0.75f,0.75f,1);
    public static final GuiElementColor ORANGE = new GuiElementColor(1,0.5f,0,1);
    public static final GuiElementColor PINK = new GuiElementColor(1,0.68f,0.68f,1);
    public static final GuiElementColor PURPLE = new GuiElementColor(0.5f,0,0.5f,1);
    public static final GuiElementColor BROWN = new GuiElementColor(0.5f,0.25f,0,1);

    @Getter
    private float red;
    @Getter
    private float green;
    @Getter
    private float blue;
    @Getter
    private float alpha;


    public int toInt() {
        return ((int) (red * 255) << 16) | ((int) (green * 255) << 8) | (int) (blue * 255) | ((int) (alpha * 255) << 24);
    }
    public void useColor(){
        GlStateManager.color(red, green, blue, alpha);
    }

    //from string hex rgba
    public static GuiElementColor fromHex(String hex) {
        return from((int)Long.parseLong(hex, 16));
    }

    public static GuiElementColor from(float red, float green, float blue, float alpha) {
        return new GuiElementColor(
                red / 255,
                green / 255,
                blue / 255,
                alpha / 255
        );
    }
    //from int rgba where a >0 <=1

    public static GuiElementColor from(int color) {
        return new GuiElementColor(
                (color >> 16 & 0xFF) / 255f,
                (color >> 8 & 0xFF) / 255f,
                (color & 0xFF) / 255f,
                (color >> 24 & 0xFF) / 255f
        );
    }

}
