package com.grandtheftwarzone.gtwmod.core.map.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.PlayerMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.RadarClient;
import com.grandtheftwarzone.gtwmod.api.utils.GLUtils;
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
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static com.grandtheftwarzone.gtwmod.api.utils.GLUtils.*;

@RegisterDisplayElement(templateId = "minimap")
public class ElementMinimap extends BaseElement {

    private int zoomRadar, zoom = 250, debug;
    private double coef, step;
    private ResourceLocation minimapImage, radarImage;
    private MapImage minimap;
    private RadarClient radarPlayer;
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

        if (!GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isInitElementDraw()) {
            init();
            return;
        }

        if (!GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isActive()) {return;}

        MapLocation cord = radarPlayer.getMapLocation("minimap");

        RenderUtils.bindTexture(minimapImage);


        float centerX = getX() + (float) getWidth() / 2;
        float centerY = getY() + (float) getHeight() / 2;
        enableCircleStencil(centerX, centerY, (float) (getHeight() /2));

        MapLocation radarCordImage = new MapLocation(cord.getX() - (zoom / 2), cord.getY() - (zoom / 2));

        drawPartialImage(getX(), getY(), getWidth(), getHeight(), radarCordImage.getX(), radarCordImage.getY(), zoom, zoom);

        // Extra filter
        if (GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getColorBorderReach() != null) {
            radarPlayer.updateColorFilter();
            RenderUtils.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getColorBorderReach().toInt(), GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getOpacityFilter());
        }

        colorFrame = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getColorFrame();

        drawHollowCircle(centerX, centerY, (float) (getHeight() /2), 7,   colorFrame);

        if (radarPlayer.inMap()) {

            // Блок для отображения маркеров.
            List<MapMarker> markerList = GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getAllMarkerFilter(minimap.getImageId());

            double proporzia = (double) zoom / getWidth();


            for (MapMarker marker : markerList) {
//                System.out.println("Draw " + marker.getName() + " " + marker.isDraw());
                if (!marker.isDraw()) {
                    continue;
                }
//                System.out.print(marker.toString());
                MapLocation location = marker.getMapLocation("minimap");
                ResourceLocation iconImage = marker.getIcon();

                double deltaX = location.getX() - cord.getX();
                double deltaY = location.getY() - cord.getY();

                double x = (deltaX / proporzia) + centerX;
                double y = (deltaY / proporzia) + centerY;

                int zoomMarker = (int) (zoomRadar + zoomRadar*0.2);

                GlStateManager.pushMatrix();
                RenderUtils.bindTexture(iconImage);
                GlStateManager.translate(x, y, 0);

                if (marker instanceof PlayerMarker) {

                    int iconX = (int) (-zoomMarker / 2);
                    int iconY = (int) (-zoomMarker / 2);
                    int iconSize = 8;


                    int borderThickness = 1;
                    AtumColor color = AtumColor.GRAY;
                    String gangMarkerStr = ((PlayerMarker) marker).getData().getSubsection("data").getStringOrNull("gang_id");
                    String playerGangId = (GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getPlayerMarker() != null) ? GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getPlayerMarker().getData().getSubsection("data").getStringOrNull("gang_id") : null;
                    if (gangMarkerStr != null && playerGangId != null && gangMarkerStr.equals(playerGangId)) {
                        color = AtumColor.LIME;
                    }


                    enableCircleStencil(centerX, centerY, (float) (getHeight() /2));

                    drawRect(iconX - borderThickness, iconY - borderThickness, iconSize + borderThickness*2, iconSize + borderThickness*2, color);


                    Gui.drawModalRectWithCustomSizedTexture(
                            (int) (-zoomMarker / 2),
                            (int) (-zoomMarker / 2),
                            iconSize, iconSize,
                            iconSize, iconSize,
                            64, 64
                    );

                } else if (marker instanceof RadarClient) {
                    GlStateManager.popMatrix();
                    continue;
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(
                            (int) (-zoomMarker / 2),
                            (int) (-zoomMarker / 2),
                            0, 0,
                            zoomMarker, zoomMarker,
                            zoomMarker, zoomMarker
                    );
                }

                GlStateManager.popMatrix();
            }

            // Цветовая подложка
            if (GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getDefaultColorFrame() != colorFrame) {
                colorFrame.useColor();
            }

            // Отображение радара
            GlStateManager.pushMatrix();
            RenderUtils.bindTexture(radarImage);
            GlStateManager.translate(getX() + ((float) getWidth() / 2), getY() + ((float) getHeight() / 2), 0);
            GlStateManager.rotate((float) Minecraft.getMinecraft().player.rotationYaw, 0, 0, 1);

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

        drawHollowCircle(centerX, centerY, (float) (getHeight() /2), 7,   colorFrame);
        disableStencil();

    }




    @SneakyThrows
    private void init() {

        // @TODO remove
        if (!GtwAPI.getInstance().getMapManagerClient().isAllowedToDisplay()) {
            return;
        }

        minimap = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage();

//        this.minimapImage = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getResourceLocation("minimapImage");
        if (minimap == null) {
            return;
        }

        minimapImage = minimap.getImage();

        this.radarImage = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getResourceLocation("radarImage");

        radarPlayer = GtwAPI.getInstance().getMapManagerClient().getRadarPlayer();
        radarPlayer.setStep(step);
        radarPlayer.setCoefficient(coef);

        DisplayRenderer renderer = getElementOwner().getDisplayRenderer();
        GtwAPI.getInstance().getMapManagerClient().getMinimapManager().updateMinimapManager(renderer);

         GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setInitElementDraw(true);
    }


    public void drawRect(int posX, int posY, int width, int height, AtumColor color) {
        // Enable scissor test and set the scissor rectangle
        GLUtils.enableScissor(getX(), getY(), getWidth(), getHeight());

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        // Draw
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        color.useColor();
        GlStateManager.disableDepth();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(posX, posY + height, 0).endVertex();
        buffer.pos(posX + width, posY + height, 0).endVertex();
        buffer.pos(posX + width, posY, 0).endVertex();
        buffer.pos(posX, posY, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f);

        // Disable scissor test
        GLUtils.disableScissor();
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
