package com.grandtheftwarzone.gtwmod.core.map.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.marker.RadarPlayer;
import lombok.SneakyThrows;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@RegisterDisplayElement(templateId = "minimap")
public class ElementMinimap extends BaseElement {

    private int zoomRadar, zoom = 250, debug;
    private double coef, step;
    private ResourceLocation minimapImage, radarImage;
    private MapImage minimap;
    private EntityLocation player;
    private RadarPlayer radarPlayer;
    private AtumColor colorFrame;


    public ElementMinimap(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float partialTicks, int mouseX, int mouseY) {

        zoom = Math.abs(Integer.parseInt(StringUtils.formatWithPlaceholders(
                getAtumMod(),
                getSettingsConfig().getSubsection("settings").getString("zoom"),
                PlaceholderContext.of(getElementOwner().getDisplayRenderer())
        )));

        boolean init = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isInitElementDraw();
        if (!init) {
            init();
            return;
        }

        if (!GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isActive()) {return;}

        player.update(Minecraft.getMinecraft().player);

        MapLocation cord = radarPlayer.getCurrentMapLocation();


        RenderUtils.bindTexture(minimapImage);
        drawPartialImage(getX(), getY(), getWidth(), getHeight(), (int) cord.getX() - (zoom / 2), (int) cord.getY() - (zoom / 2), zoom, zoom);

        // Extra filter
        if (GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getColorBorderReach() != null) {
            radarPlayer.updateColorFilter();
            RenderUtils.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getColorBorderReach().toInt(), GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getOpacityFilter());
        }


        colorFrame = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getColorFrame();

        RenderUtils.drawOutline(getX(), getY(), getWidth(), getHeight(), 2, colorFrame);
//        drawCustomCircle(getX() + getWidth()/2, getY() + getHeight()/2, (float) ((getHeight() /2) + 2.5), 5, colorFrame.toInt());
//        clipOutsideCircleAndInsideRectangle(getX() + getWidth()/2, getY() + getHeight()/2, (float) ((getHeight() /2) + 2.5), getWidth(), getHeight());
        if (radarPlayer.inMap()) {
            GlStateManager.pushMatrix();
            RenderUtils.bindTexture(radarPlayer.getIcon());
            GlStateManager.translate(getX() + ((float) getWidth() / 2), getY() + ((float) getHeight() / 2), 0);
            GlStateManager.rotate((float) player.getYaw(), 0, 0, 1);

            Gui.drawModalRectWithCustomSizedTexture(
                    (int) (-zoomRadar / 2),
                    (int) (-zoomRadar / 2),
                    0, 0,
                    zoomRadar, zoomRadar,
                    zoomRadar, zoomRadar
            );
            GlStateManager.popMatrix();
        } else {
            // No connection
            drawText((int) (getX() + (getWidth() / 4)), (int) (getY() + getHeight() / 2.5), "NO SIGNAL", AtumColor.WHITE);
        }


    }

    public static void clipOutsideCircleAndInsideRectangle(int centerX, int centerY, float radius, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int x = (int) (centerX - radius);
        int y = (int) (centerY - radius);
        int x2 = (int) (centerX + radius);
        int y2 = (int) (centerY + radius);

        int left = Math.max(x, 0);
        int right = Math.min(x2, width);
        int top = Math.max(y, 0);
        int bottom = Math.min(y2, height);

        GL11.glScissor(left, top, right - left, bottom - top);
    }

    public static void drawCustomCircle(int centerX, int centerY, float radius, int thickness, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glColor4f(
                ((color >> 16) & 0xFF) / 255.0f,
                ((color >> 8) & 0xFF) / 255.0f,
                (color & 0xFF) / 255.0f,
                ((color >> 24) & 0xFF) / 255.0f
        );

        GL11.glLineWidth(thickness);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < 360; i++) {
            float radian = (float) Math.toRadians(i);
            float x = centerX + radius * (float) Math.cos(radian);
            float y = centerY + radius * (float) Math.sin(radian);
            GL11.glVertex2f(x, y);
        }
        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    public static void drawPartialImage(int posX, int posY, int width, int height, int textureX, int textureY, int texturePartWidth, int texturePartHeight) {
        double imageWidth = (double) GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        double imageHeight = (double) GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        double einsTeilerWidth = 1.0 / imageWidth;
        double uvWidth = einsTeilerWidth * (double)texturePartWidth;
        double uvX = einsTeilerWidth * (double)textureX;
        double einsTeilerHeight = 1.0 / imageHeight;
        double uvHeight = einsTeilerHeight * (double)texturePartHeight;
        double uvY = einsTeilerHeight * (double)textureY;

        // Set the clamping mode for the texture
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslatef((float)posX, (float)posY, 0.0F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(uvX, uvY);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2d(uvX, uvY + uvHeight);
        GL11.glVertex3f(0.0F, (float)height, 0.0F);
        GL11.glTexCoord2d(uvX + uvWidth, uvY + uvHeight);
        GL11.glVertex3f((float)width, (float)height, 0.0F);
        GL11.glTexCoord2d(uvX + uvWidth, uvY);
        GL11.glVertex3f((float)width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glTranslatef((float)(-posX), (float)(-posY), 0.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void drawText(int x, int y, String text, AtumColor color) {

        float red = color.getRed() / 255.0f;
        float green = color.getGreen() / 255.0f;
        float blue = color.getBlue() / 255.0f;
        float alpha = color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawString(text, x, y, color.toInt(), true);

    }




    @SneakyThrows
    private void init() {

        // @TODO remove
        if (!GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isAllowedToDisplay()) {
            return;
        }

        minimap = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage();

//        this.minimapImage = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getResourceLocation("minimapImage");
        if (minimap == null) {
            return;
        }

        minimapImage = minimap.getImage();

        this.radarImage = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getResourceLocation("radarImage");

        player = new EntityLocation(Minecraft.getMinecraft().player);
        radarPlayer = new RadarPlayer(player, minimap, radarImage, coef, step);

        DisplayRenderer renderer = getElementOwner().getDisplayRenderer();
        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().updateMinimapManager(renderer);

         GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setInitElementDraw(true);
    }


    @Override
    public void updateElementVariables(@NotNull Config config) {

        debug = Math.abs(config.getInt("debug"));
        coef = Math.abs(config.getInt("coef"));
        step = Math.abs(config.getInt("step"));
        zoomRadar = (config.getInt("zoomRadar") / 2);
        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setInitElementDraw(false);
    }


    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

}
