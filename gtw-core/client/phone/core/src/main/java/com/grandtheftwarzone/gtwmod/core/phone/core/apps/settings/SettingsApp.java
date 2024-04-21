package com.grandtheftwarzone.gtwmod.core.phone.core.apps.settings;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhoneApp;
import me.phoenixra.atumconfig.api.config.Config;


import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@RegisterPhoneApp
public class SettingsApp implements PhoneApp {

    private ResourceLocation icon = new ResourceLocation("gtwmod", "textures/gui/phone/apps/test.png");


    private CanvasPhoneApp canvas;
    private Config config;


    @Override
    public void draw(@NotNull CanvasPhone parent,
                     @NotNull DisplayResolution resolution,
                     float scaleFactor,
                     int displayWidth, int displayHeight,
                     int mouseX, int mouseY) {
        RenderUtils.fill(
                0,
                0,
                displayWidth,
                displayHeight,
                AtumColor.ORANGE.toInt(),
                1.0f
        );
        canvas.draw(
                resolution,
                scaleFactor,
                mouseX,
                mouseY
        );

    }

    @Override
    public void drawIcon(@NotNull DisplayResolution resolution,
                         int x, int y, int size,
                         boolean isHovered) {
        RenderUtils.bindTexture(icon);
        AtumColor.WHITE.useColor();
        if(isHovered) {
            GlStateManager.color(
                    0.85f,
                    0.85f,
                    0.85f,
                    1.0f
            );
        }
        RenderUtils.drawCompleteImage(
                x,
                y,
                size,
                size
        );
    }

    @Override
    public void updateVariables(@NotNull CanvasPhone canvasPhone,
                                @NotNull Config config) {
        if(canvas!=null) {
            canvas.onRemove();
        }
        this.config = config;
    }


    @Override
    public void onAppOpen(@NotNull CanvasPhone parent) {
        if(canvas!=null) {
            canvas.onRemove();
        }
        canvas = new CanvasPhoneApp(
                GtwAPI.getInstance().getGtwMod(),
                parent
        );
        canvas.updateVariables(
                config,
                "canvas"
        );
    }

    @Override
    public void onAppClose(@NotNull CanvasPhone parent) {
        if(canvas!=null) {
            canvas.onRemove();
            canvas = null;
        }
    }

    @Override
    public @NotNull PhoneShape getShapeRequired() {
        return PhoneShape.VERTICAL;
    }

    @Override
    public int getAppPriority() {
        return 0;
    }

    @Override
    public @NotNull String getAppName() {
        return "Settings";
    }

    @Override
    public @NotNull String getId() {
        return "settings";
    }
}
