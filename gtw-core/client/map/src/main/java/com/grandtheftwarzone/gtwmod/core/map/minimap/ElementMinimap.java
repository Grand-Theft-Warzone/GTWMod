package com.grandtheftwarzone.gtwmod.core.map.minimap;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.MapImage;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.api.map.marker.RadarPlayer;
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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.grandtheftwarzone.gtwmod.api.utils.GLUtils.*;

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

        if (!GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isInitElementDraw()) {
            init();
            return;
        }

        if (!GtwAPI.getInstance().getMapManagerClient().getMinimapManager().isActive()) {return;}

        player.update(Minecraft.getMinecraft().player);

        MapLocation cord = radarPlayer.getMapLocation("minimap");

        RenderUtils.bindTexture(minimapImage);


        float centerX = getX() + (float) getWidth() /2;
        float centerY = getY() + (float) getHeight() /2;
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
                MapLocation location = marker.getMapLocation("minimap");
                ResourceLocation iconImage = marker.getIcon();

                double deltaX = location.getX() - cord.getX();
                double deltaY = location.getY() - cord.getY();

                double x = (deltaX / proporzia) + centerX;
                double y = (deltaY / proporzia) + centerY;

//                System.out.println("PROP: " + proporzia);
//                System.out.println("DeltaX: " + deltaX + " DeltaY: " + deltaY);
//                System.out.println("X: " + x + " Y: " + y);
//                System.out.println("CX: " + centerX + " CY: " + centerY);
                GlStateManager.pushMatrix();
                RenderUtils.bindTexture(iconImage);
                GlStateManager.translate(x, y, 0);
                Gui.drawModalRectWithCustomSizedTexture(
                        (int) (-zoomRadar / 2),
                        (int) (-zoomRadar / 2),
                        0, 0,
                        zoomRadar, zoomRadar,
                        zoomRadar, zoomRadar
                );
                GlStateManager.popMatrix();

            }

            if (GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getDefaultColorFrame() != colorFrame) {
                colorFrame.useColor();
            }
            GlStateManager.pushMatrix();
            RenderUtils.bindTexture(radarImage);
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

        player = new EntityLocation(Minecraft.getMinecraft().player);

        radarPlayer = GtwAPI.getInstance().getMapManagerClient().getRadarPlayer();
        radarPlayer.setStep(step);
        radarPlayer.setCoefficient(coef);

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
