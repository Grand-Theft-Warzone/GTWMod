package com.grandtheftwarzone.gtwmod.core.map.globalmap.element;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.PlayerMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.RadarClient;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas.CanvasGlobalmap;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.data.DataDrawTextMarker;
import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementMarker extends BaseElement {

    @Getter
    private MapMarker marker;

    private final float[] brightnessOnHover = new float[]{0.65F,0.65F,0.65F};

    @Getter
    private boolean haver = false;

    private long haverTimer = 0;

    public ElementMarker(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner, MapMarker marker) {
        super(atumMod, elementOwner);
        this.marker = marker;

    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

        if (!marker.isDraw()) {
            return;
        }

        haver = isHovered(getLastMouseX(), getLastMouseY());
        if (haver) {
            haverTimer += 1;
            System.out.println(haverTimer);
        } else {
            haverTimer = 0;
        }

        // Отображение маркеров.
        if (marker instanceof PlayerMarker) {

            GlStateManager.pushMatrix();
            RenderUtils.bindTexture(marker.getIcon());
            GlStateManager.translate(getX(), getY(), 0);

            if (haver) {
                GlStateManager.color(
                        brightnessOnHover[0],
                        brightnessOnHover[1],
                        brightnessOnHover[2],
                        1.0f
                );
            }

            int borderThickness = 1;
            AtumColor color = AtumColor.GRAY;
            String gangMarkerStr = ((PlayerMarker) marker).getData().getSubsection("data").getStringOrNull("gang_id");
            String playerGangId = (GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getPlayerMarker() != null) ? GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getPlayerMarker().getData().getSubsection("data").getStringOrNull("gang_id") : null;
            if (gangMarkerStr != null && gangMarkerStr.equals(playerGangId)) {
                color = AtumColor.LIME;
            }
            RenderUtils.drawRect(-borderThickness, -borderThickness, getWidth() + borderThickness*2, getHeight() + borderThickness*2, color);

            Gui.drawModalRectWithCustomSizedTexture(0, 0,
                    8, 8,
                    8, 8,
                    64, 64
            );
            GlStateManager.popMatrix();

        } else if (marker instanceof RadarClient) {

            GlStateManager.pushMatrix();
            RenderUtils.bindTexture(marker.getIcon());
            GlStateManager.translate(getX() + (float) getWidth() / 2, getY() + (float) getHeight() / 2, 0);
            if (haver) {
                GlStateManager.color(
                        brightnessOnHover[0],
                        brightnessOnHover[1],
                        brightnessOnHover[2],
                        1.0f
                );
            }
            GlStateManager.rotate((float) Minecraft.getMinecraft().player.rotationYaw, 0, 0, 1);
            Gui.drawModalRectWithCustomSizedTexture(
                    -getWidth() / 2,
                    -getHeight() / 2,
                    0, 0,
                    getWidth(), getHeight(),
                    getWidth(), getHeight()
            );
            GlStateManager.popMatrix();

        } else {
            // Любой другой маркер
            RenderUtils.bindTexture(marker.getIcon());
            if (haver) {
                GlStateManager.color(
                        brightnessOnHover[0],
                        brightnessOnHover[1],
                        brightnessOnHover[2],
                        1.0f
                );
            }
            RenderUtils.drawCompleteImage(getX(), getY(), getWidth(), getWidth());
        }

//        // Отрисовка границы вокруг элемента
//        int outlineSize = 1; // Толщина границы
//        AtumColor color = AtumColor.RED; // Цвет границы
//        RenderUtils.drawOutline(this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), outlineSize, color);

        if (isHovered(getLastMouseX(), getLastMouseY())) {
            System.out.println("Наведение на " + marker.getName());
        }

        if (haverTimer > 200) {
            System.out.println("Show ");
//            GLUtils.drawRoundedRect(getX(), getY(), getWidth()*2, getHeight()*2, 16, AtumColor.RED);
//            RenderUtils.drawRect(getX(), getY(), 50/2, 10/2, AtumColor.WHITE);
            CanvasGlobalmap elementOwner = (CanvasGlobalmap) getElementOwner();
            int sizeText = 8;
            float proporziaSize = (float) sizeText / 12;
            String markerName = !StringUtils.removeColorCodes(marker.getName() != null ? marker.getName() : "").isEmpty() ? StringUtils.removeColorCodes(marker.getName()) : "?";
            int textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(markerName) * proporziaSize);
            int textHeight = (int) (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * proporziaSize);
            int textPosX = (int) (getX() + getWidth()/2 - textWidth/2);
            int textPosY = getY() + getHeight() + getHeight()/10;
            AtumColor color = AtumColor.WHITE;
            elementOwner.setDrawTextMarker(new DataDrawTextMarker(textPosX, textPosY, textWidth, textHeight, markerName, sizeText, color));
        }

        // Сброс цвета обратно на белый после отрисовки
        if (haver) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }


    public void setXY(int x, int y) {
        getOriginX().setDefaultValue(x);
        getOriginY().setDefaultValue(y);
    }

    public void setSize(int size) {
        getOriginWidth().setDefaultValue(size);
        getOriginHeight().setDefaultValue(size);
    }

    public void setXY(MapLocation location) {
        this.setXY((int) location.getX(), (int) location.getY());
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
