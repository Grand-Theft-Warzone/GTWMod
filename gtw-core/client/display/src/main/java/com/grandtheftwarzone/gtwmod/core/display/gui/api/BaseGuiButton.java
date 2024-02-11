package com.grandtheftwarzone.gtwmod.core.display.gui.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BaseGuiButton extends GuiButton {
    private final BaseGUI gui;

    protected ResourceLocation BUTTON_IMAGE_DEFAULT;
    protected ResourceLocation BUTTON_IMAGE_HOVERED;
    protected ResourceLocation BUTTON_IMAGE_CLICKED;

    protected ResourceLocation currentTexture;

    private List<Runnable> onCLick = new ArrayList<>();
    private long lastClick = 0;

    protected int guiX, guiY;
    protected boolean pressed;
    public BaseGuiButton(BaseGUI gui,
                         int buttonId,
                         int x,
                         int y,
                         int width,
                         int height) {
        super(buttonId, x, y, width, height, "");
        this.gui = gui;
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        pressed = false;
        if(System.currentTimeMillis()-lastClick>500) {
            lastClick = System.currentTimeMillis();
            onCLick.forEach(Runnable::run);
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        super.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(this.visible && isMouseOver()) {
            pressed = true;
            return true;
        }
        return false;
    }


    public BaseGuiButton setImageDefault(ResourceLocation res){
        BUTTON_IMAGE_DEFAULT = res;
        return this;
    }
    public BaseGuiButton setImageOnHover(ResourceLocation res){
        BUTTON_IMAGE_HOVERED = res;
        return this;
    }
    public BaseGuiButton setImageOnClick(ResourceLocation res){
        BUTTON_IMAGE_CLICKED = res;
        return this;
    }
    public BaseGuiButton addActionOnClick(Runnable action){
        onCLick.add(action);
        return this;
    }

    @Override
    public void drawButton(Minecraft mc,int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            int x = guiX + this.x;
            int y = guiY + this.y;
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(currentTexture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(x, y, 0, 0, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, x + this.width / 2, y + (this.height - 8) / 2, j);
        }
    }
}
