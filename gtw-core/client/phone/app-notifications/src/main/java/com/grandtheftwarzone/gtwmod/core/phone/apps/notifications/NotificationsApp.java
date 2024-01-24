package com.grandtheftwarzone.gtwmod.core.phone.apps.notifications;

import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@RegisterPhoneApp
public class NotificationsApp implements PhoneApp {
    private ResourceLocation icon = new ResourceLocation("gtwmod", "textures/main_menu/discord_button.png");


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
    }

    @Override
    public void drawIcon(@NotNull CanvasPhone parent,
                         @NotNull DisplayResolution resolution,
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

    }

    @Override
    public void onPhoneOpen(@NotNull CanvasPhone parent) {

    }


    @Override
    public boolean onPressedBack(CanvasPhone parent) {
        return true;
    }


    @Override
    public @NotNull PhoneShape getShapeRequired() {
        return PhoneShape.HORIZONTAL;
    }

    @Override
    public int getAppPriority() {
        return 1;
    }

    @Override
    public @NotNull String getAppName() {
        return "Notifications";
    }

    @Override
    public @NotNull String getId() {
        return "notifications";
    }
}
