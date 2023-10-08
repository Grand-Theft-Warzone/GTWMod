package com.grandtheftwarzone.gtwclient.core.display.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class HudElement {

    /** The values of the color red */
    public static final int COLOR_RED = 0xC10000;

    /** The values of the color red */
    public static final int COLOR_PINK = 0xFF69B4;

    /** The values of the color red */
    public static final int COLOR_BROWN = 0x8b4513;

    /** The values of the color white */
    public static final int COLOR_WHITE = 0xF2F2F2;

    /** The values of the color white */
    public static final int COLOR_ORANGE = 0xFF8400;

    /** The values of the color green */
    public static final int COLOR_GREEN = 0x3BC200;

    /** The values of the color red */
    public static final int COLOR_PURPLE = 0xA400F0;

    /** The values of the color blue */
    public static final int COLOR_BLUE = 0x005BC2;

    /** The values of the color blue */
    public static final int COLOR_AQUA = 0x00FFFF;

    /** The value of the color black */
    public static final int COLOR_BLACK = 0x292929;

    /** The values of the color grey */
    public static final int COLOR_GREY = 0x8A8A8A;

    /** The values of the color yellow */
    public static final int COLOR_YELLOW = 0xEEEE00;

    /** The values of the default color */
    public static final int[] COLOR_DEFAULT = { 0x4C4C4C, 0x3D3D3D };

    /** ResourceLocation of the interface texture for the Player-HUD */
     protected static final ResourceLocation FIST_ICON = new ResourceLocation("gtwclient:textures/hud/fist_icon.png");

    /** The x coordinate the element will be rendered at on the screen */
    protected int posX;
    /** The y coordinate the element will be rendered at on the screen */
    protected int posY;

    /**
     * The default x coordinate the element will be rendered at on the screen
     */
    protected final int defaultPosX;
    /**
     * The default y coordinate the element will be rendered at on the screen
     */
    protected final int defaultPosY;

    /** The width of this element */
    protected int elementWidth;
    /** The height of this element */
    protected int elementHeight;
    /** The Type of this element */
    protected HudElementType type;

    /** The Minecraft instance */
    protected Minecraft mc;


    protected double scale;
    protected double scaleInverted;

    public HudElementType parent;

    public HudElement(HudElementType type, int posX, int posY, int width, int height) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.defaultPosX = posX;
        this.defaultPosY = posY;
        this.elementWidth = width;
        this.elementHeight = height;
        this.mc = Minecraft.getMinecraft();
        this.scale = 1D;
        this.scaleInverted = 1D / this.scale;
        this.parent = type;
    }

    /**
     * Function called to draw this element on the screen
     */
    public void draw(Gui gui, int scaledWidth, int scaledHeight) {

        GlStateManager.scale(this.scale, this.scale, this.scale);

        this.drawElement(gui, scaledWidth, scaledHeight);

        GlStateManager.scale(this.scaleInverted, this.scaleInverted, this.scaleInverted);
    }

    public abstract void drawElement(Gui gui, int scaledWidth, int scaledHeight);


    public int getPosX(int scaledWidth) {
        return this.posX;
    }
    public int getPosY(int scaledHeight) {
        return this.posY;
    }


    public int getWidth(int scaledWidth) {
        return this.elementWidth;
    }
    public int getHeight(int scaledHeight) {
        return this.elementHeight;
    }

    public double getScale() {
        return 1;
    }

    public double getInvertedScale() {
        return 1 / getScale();
    }


    public HudElementType getType() {
        return this.type;
    }


    public boolean setPos(int posX, int posY) {
        boolean xValid = false;
        boolean yValid = false;
        if(posX >= 0 && posX < (this.mc.displayWidth - this.elementWidth)) {
            xValid = true;
        }
        if(posY >= 0 && posY < (this.mc.displayHeight - this.elementHeight)) {
            yValid = true;
        }
        if(xValid && yValid) {
            this.posX = posX;
            this.posY = posY;
        }
        return xValid && yValid;
    }


    public void setPositionToDefault() {
        this.posX = this.defaultPosX;
        this.posY = this.defaultPosY;
    }



    public static void drawRect(int posX, int posY, int width, int height, int color) {
        if(color == -1)
            return;
        float f3;
        if(color <= 0xFFFFFF && color >= 0)
            f3 = 1.0F;
        else
            f3 = (color >> 24 & 255) / 255.0F;
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        //draw
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(f, f1, f2, f3);
        GlStateManager.disableDepth();
        buffer.begin(7, DefaultVertexFormats.POSITION);
        buffer.pos(posX,
                (double) posY + height,
                0
        ).endVertex();
        buffer.pos((double) posX + width,
                (double) posY + height,
                0
        ).endVertex();
        buffer.pos((double) posX + width,
                posY,
                0
        ).endVertex();
        buffer.pos(posX,
                posY,
                0
        ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f);
    }

    protected static void drawOutline(int x, int y, int width, int height, int color) {
        drawRect( x, y, width, 1, color);
        drawRect(x, y+1, 1, height-2, color);
        drawRect(x + width - 1, y+1, 1, height-2, color);
        drawRect( x, y + height - 1, width, 1, color);
    }



    public static void drawCustomBar(int x, int y, int width, int height, double value, int colorBarLight, int colorBarDark, boolean outlined) {
        drawCustomBar(x, y, width, height, value, HudElement.COLOR_DEFAULT[0], HudElement.COLOR_DEFAULT[1], colorBarLight, colorBarDark, outlined, 0x000000);
    }


    public static void drawCustomBar(int x, int y, int width, int height, double value, int colorGroundLight, int colorGroundDark, int colorBarLight,
                                     int colorBarDark) {
        drawCustomBar(x, y, width, height, value, colorGroundLight, colorGroundDark, colorBarLight, colorBarDark, true, 0x000000);
    }


    public static void drawCustomBar(int x, int y, int width, int height, double value, int colorGroundLight, int colorGroundDark, int colorBarLight,
                                     int colorBarDark, boolean outlined) {
        drawCustomBar(x, y, width, height, value, colorGroundLight, colorGroundDark, colorBarLight, colorBarDark, outlined, 0x000000);
    }


    public static void drawCustomBar(int x, int y, int width, int height, double value, int colorGroundLight, int colorGroundDark, int colorBarLight,
                                     int colorBarDark, int colorOutline) {
        drawCustomBar(x, y, width, height, value, colorGroundLight, colorGroundDark, colorBarLight, colorBarDark, true, colorOutline);
    }


    public static void drawCustomBar(int x, int y, int width, int height, double value, int colorGroundLight, int colorGroundDark, int colorBarLight,
                                     int colorBarDark, boolean outlined, int colorOutline) {
        if (value < 0.0D) {
            value = 0.0D;
        }else if (value > 100D) {
            value = 100D;
        }

        int offset = 1;

        int filledWidth = width;
        filledWidth = width - (offset * 2);
        if (filledWidth < 0)
            filledWidth = 0;
        int filledHeight = width;
        filledHeight = height - (offset * 2);
        if (filledHeight < 0)
            filledHeight = 0;

        int percentFilled = (int) Math.round(value / 100.0D * filledWidth);

        if (outlined)
            drawOutline(x, y, width, height, colorOutline);
        int halfedFilledHeight = filledHeight / 2;

        drawRect(x + offset, y + offset, percentFilled, halfedFilledHeight, colorBarLight);
        drawRect(x + offset, y + offset + halfedFilledHeight, percentFilled, filledHeight - halfedFilledHeight, colorBarDark);

        if (colorGroundDark != -1 && colorGroundLight != -1 && filledWidth - percentFilled > 0) {
            drawRect(x + offset + percentFilled, y + offset, filledWidth - percentFilled, halfedFilledHeight, colorGroundLight);
            drawRect(x + offset + percentFilled, y + offset + halfedFilledHeight, filledWidth - percentFilled, filledHeight - halfedFilledHeight, colorGroundDark);
        }
    }


    public void drawTetragon(int posX1, int posX2, int posY1, int posY2, int width1, int width2, int height1, int height2, int color) {
        if(color == -1)
            return;
        if(width1 < 0) width1 = 0;
        if(width2 < 0) width2 = 0;
        float f3;
        if(color <= 0xFFFFFF && color >= 0)
            f3 = 1.0F;
        else
            f3 = (color >> 24 & 255) / 255.0F;
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(
                GL11.GL_SRC_ALPHA,
                GL11.GL_ONE_MINUS_SRC_ALPHA,
                GL11.GL_ONE,
                GL11.GL_ZERO
        );
        //draw
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(f, f1, f2, f3);
        GlStateManager.disableDepth();
        buffer.begin(7, DefaultVertexFormats.POSITION);
        buffer.pos(posX1,
                (double) posY1 + height1,
                0
        ).endVertex();
        buffer.pos((double) posX2 + width2,
                (double) posY2 + height2,
                0
        ).endVertex();
        buffer.pos((double) posX1 + width1,
                posY2,
                0
        ).endVertex();
        buffer.pos(posX2,
                posY1,
                0
        ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f);
    }

    public static int offsetColorPercent(int color, int offsetPercent) {
        int colorOffset;

        int colorPart = (color >> 16 & 255);
        colorPart -= colorPart / (100 / offsetPercent);
        if(colorPart > 0xFF)
            colorPart = 0xFF;
        else if(colorPart < 0)
            colorPart = 0;

        colorOffset = colorPart << 16;
        colorPart = (color >> 8 & 255);
        colorPart -= colorPart / (100 / offsetPercent);
        if(colorPart > 0xFF)
            colorPart = 0xFF;
        else if(colorPart < 0)
            colorPart = 0;

        colorOffset += colorPart << 8;
        colorPart = (color & 255);
        colorPart -= colorPart / (100 / offsetPercent);
        if(colorPart > 0xFF)
            colorPart = 0xFF;
        else if(colorPart < 0)
            colorPart = 0;
        colorOffset += colorPart;
        return colorOffset;
    }

    public static int offsetColor(int color, int offset) {
        int colorOffset;

        int colorPart = (color >> 16 & 255);
        colorPart += (offset >> 16 & 255);
        if(colorPart > 0xFF)
            colorPart = 0xFF;
        else if(colorPart < 0)
            colorPart = 0;

        colorOffset = colorPart << 16;
        colorPart = (color >> 8 & 255);
        colorPart += (offset >> 8 & 255);
        if(colorPart > 0xFF)
            colorPart = 0xFF;
        else if(colorPart < 0)
            colorPart = 0;

        colorOffset += colorPart << 8;
        colorPart = (color & 255);
        colorPart += (offset & 255);
        if(colorPart > 0xFF)
            colorPart = 0xFF;
        else if(colorPart < 0)
            colorPart = 0;
        colorOffset += colorPart;
        return colorOffset;
    }


    protected void bind(ResourceLocation res) {
        this.mc.getTextureManager().bindTexture(res);
    }


    protected static ResourceLocation getPlayerSkin(AbstractClientPlayer player) {
        return player.getLocationSkin();
    }

    protected void drawStringWithBackground(String text, int posX, int posY, int colorMain, int colorBackground) {
        mc.fontRenderer.drawString(text, posX + 1, posY, colorBackground);
        mc.fontRenderer.drawString(text, posX - 1, posY, colorBackground);
        mc.fontRenderer.drawString(text, posX, posY + 1, colorBackground);
        mc.fontRenderer.drawString(text, posX, posY - 1, colorBackground);
        mc.fontRenderer.drawString(text, posX, posY, colorMain);
        GlStateManager.enableBlend();
    }


    public void renderItemIntoGUI(ItemStack stack, int x, int y, float xScale, float yScale)
    {
        this.renderItemModelIntoGUI(stack, x, y, xScale,yScale, mc.getRenderItem().getItemModelWithOverrides(stack, null, null));
    }
    protected void renderItemModelIntoGUI(ItemStack stack, int x, int y, float xScale, float yScale, IBakedModel bakedmodel)
    {
        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, xScale, yScale, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        mc.getRenderItem().renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }
    private void setupGuiTransform(int xPosition, int yPosition, float xScale, float yScale, boolean isGui3d)
    {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + mc.getRenderItem().zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(xScale, yScale, 16.0F);

        if (isGui3d)
        {
            GlStateManager.enableLighting();
        }
        else
        {
            GlStateManager.disableLighting();
        }
    }
}
