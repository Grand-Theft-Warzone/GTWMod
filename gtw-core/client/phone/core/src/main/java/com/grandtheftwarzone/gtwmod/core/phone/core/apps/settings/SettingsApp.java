package com.grandtheftwarzone.gtwmod.core.phone.core.apps.settings;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.choose.ElementChooseBool;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@RegisterPhoneApp
public class SettingsApp implements PhoneApp {

    private ResourceLocation icon = new ResourceLocation("gtwmod", "textures/gui/phone/apps/test.png");


    private ElementChooseBool chooseAnimate;


    @Override
    public void draw(@NotNull CanvasPhone parent,
                     @NotNull DisplayResolution resolution,
                     float scaleFactor,
                     int displayWidth, int displayHeight,
                     int mouseX, int mouseY) {
        chooseAnimate.draw(
                resolution,
                scaleFactor,
                mouseX,
                mouseY
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
        chooseAnimate = new ElementChooseBool(
                GtwAPI.getInstance().getGtwMod(),
                canvasPhone
        );
        chooseAnimate.updateVariables(
                config.getSubsection("choose_animate"),
                "animate"
        );

    }


    @Override
    public void onPhoneOpen(@NotNull CanvasPhone parent) {
        Config config = chooseAnimate.getSettingsConfig();
        chooseAnimate = new ElementChooseBool(
                GtwAPI.getInstance().getGtwMod(),
                parent
        );
        chooseAnimate.updateVariables(
                config,
                "animate"
        );
    }

    @Override
    public boolean onPressedBack(CanvasPhone parent) {
        return true;
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
