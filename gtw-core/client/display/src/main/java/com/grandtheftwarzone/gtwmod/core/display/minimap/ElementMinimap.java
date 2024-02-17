package com.grandtheftwarzone.gtwmod.core.display.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.minimap.MapImage;
import net.minecraft.client.gui.FontRenderer;
import com.grandtheftwarzone.gtwmod.api.misc.ColorFilter;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.display.minimap.marker.RadarPlayer;
import lombok.SneakyThrows;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import net.minecraft.client.Minecraft;
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
    private MapLocation N1, N2, N3, N4;
    private ResourceLocation minimapImage, radarImage;
    private MapImage minimap;
    private EntityLocation player;
    private RadarPlayer radarPlayer;
    private AtumColor colorFrame;
    private boolean init = false;


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

        if (!init) {
            init();
            return;
        }

        if (!GtwAPI.getInstance().getGtwMinimapManager().isActive()) {return;}

        player.update(Minecraft.getMinecraft().player);

        MapLocation cord = radarPlayer.getCurrentMapLocation();

        RenderUtils.bindTexture(minimapImage);
        drawPartialImage(getX(), getY(), getWidth(), getHeight(), (int) cord.getX() - (zoom / 2), (int) cord.getY() - (zoom / 2), zoom, zoom);

        // Extra filter
        radarPlayer.updateColorFilter();
        ColorFilter filter = GtwAPI.getInstance().getGtwMinimapManager().getColorFilter();
        System.out.println("Color " + filter.getOpacity());
        RenderUtils.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), filter.getColor().toInt(), filter.getOpacity());


        // No connection
        boolean inMap = radarPlayer.inMap();
        if (!inMap) {
            drawText(getX() + (getWidth() / 4), (int) (getY() + getHeight() / 2.5), "NO SIGNAL", AtumColor.WHITE);
        }

        colorFrame = GtwAPI.getInstance().getGtwMinimapManager().getColorFrame();

        RenderUtils.drawOutline(getX(), getY(), getWidth(), getHeight(), 2, colorFrame);


        if (inMap) {
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
        }


    }

//    public ResourceLocation modifyMinimap() {
//        ResourceLocation originalLocation = new ResourceLocation("gtwmod", "textures/gui/minimap/test_map.png");
//        ResourceLocation modifiedLocation = new ResourceLocation("gtwmod", "textures/gui/minimap/modified_map.png");
//
//        try {
//            InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(originalLocation).getInputStream();
//            BufferedImage image = ImageIO.read(stream);
//
//            int width = image.getWidth();
//            int height = image.getHeight();
//
//            for (int y = 0; y < height; y++) {
//                image.setRGB(0, y, 0xFF000000); // Левый край
//                image.setRGB(width - 1, y, 0xFF000000); // Правый край
//            }
//            for (int x = 0; x < width; x++) {
//                image.setRGB(x, 0, 0xFF000000); // Верхний край
//                image.setRGB(x, height - 1, 0xFF000000); // Нижний край
//            }
//
//            String modFolderLocation = "mods/gtwmod/textures/gui/minimap/";
//            File modFolder = new File(modFolderLocation);
//            if (!modFolder.exists()) {
//                modFolder.mkdirs();
//            }
//
//            File modifiedFile = new File(modFolder, "modified_map.png");
//            if (!modifiedFile.exists()) {
//                System.out.println("DELITE");
//            }
//
//            modifiedFile.createNewFile();
//
//            FileOutputStream outputStream = new FileOutputStream(modifiedFile);
//            ImageIO.write(image, "PNG", outputStream);
//            outputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return modifiedLocation;
//    }


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
        fontRenderer.drawString(text, 0, 0, color.toInt(), true);

    }




    @SneakyThrows
    private void init() {

        this.minimapImage = GtwAPI.getInstance().getGtwMinimapManager().getResourceLocation("minimapImage");
        this.radarImage = GtwAPI.getInstance().getGtwMinimapManager().getResourceLocation("radarImage");

        minimap = new MapImage(minimapImage, N4, N3, N2, N1);
        player = new EntityLocation(Minecraft.getMinecraft().player);
        radarPlayer = new RadarPlayer(player, minimap, radarImage, coef, step);

        DisplayRenderer renderer = getElementOwner().getDisplayRenderer();
        GtwAPI.getInstance().getGtwMinimapManager().updateMinimapManager(renderer);

        init = true;
    }


    @Override
    public void updateElementVariables(@NotNull Config config) {

        debug = Math.abs(config.getInt("debug"));
        coef = Math.abs(config.getInt("coef"));
        step = Math.abs(config.getInt("step"));
        zoomRadar = (config.getInt("zoomRadar") / 2);

        String[] CN1 = config.getString("N1").split(";");
        String[] CN2 = config.getString("N2").split(";");
        String[] CN3 = config.getString("N3").split(";");
        String[] CN4 = config.getString("N4").split(";");

        N1 = new MapLocation(Integer.parseInt(CN1[0]), Integer.parseInt(CN1[1]));
        N2 = new MapLocation(Integer.parseInt(CN2[0]), Integer.parseInt(CN2[1]));
        N3 = new MapLocation(Integer.parseInt(CN3[0]), Integer.parseInt(CN3[1]));
        N4 = new MapLocation(Integer.parseInt(CN4[0]), Integer.parseInt(CN4[1]));
        init = false;
    }


    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        if (minimap != null || player != null || radarPlayer != null) {
            minimap = new MapImage(minimapImage, N4, N3, N3, N1);
            player = new EntityLocation(Minecraft.getMinecraft().player);
            radarPlayer = new RadarPlayer(player, minimap, coef, step);
        }
        return baseElement;
    }

}
