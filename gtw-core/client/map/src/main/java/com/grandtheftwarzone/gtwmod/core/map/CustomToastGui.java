package com.grandtheftwarzone.gtwmod.core.map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CustomToastGui implements IToast {
    private static final ResourceLocation TOAST_TEXTURES = new ResourceLocation("textures/gui/toasts.png");
    private final ResourceLocation icon;
    private final String title;
    private final String description;
    private final long displayDuration;
    private boolean hasPlayedSound;

    public CustomToastGui(ResourceLocation icon, String title, String description, long displayDuration) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.displayDuration = displayDuration;
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta) {
        Minecraft mc = toastGui.getMinecraft();
        mc.getTextureManager().bindTexture(TOAST_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

        mc.fontRenderer.drawString(title, 30, 7, -256);
        mc.fontRenderer.drawString(description, 30, 18, -1);

        if (icon != null) {
            mc.getTextureManager().bindTexture(icon);
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            toastGui.drawTexturedModalRect(8, 8, 0, 0, 16, 16);
        }

        return delta >= displayDuration ? Visibility.HIDE : Visibility.SHOW;
    }

    public static void showToast(String title, String description, ResourceLocation resourceLocation, int durationSeconds) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiToast toastGui = mc.getToastGui();
        CustomToastGui toast = new CustomToastGui(resourceLocation, title, description, durationSeconds * 1000L);
        toastGui.add(toast);
    }
}
