package me.phoenixra.gtwclient.mainmenu;

import lombok.Setter;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonAdvanced extends GuiButton {

    protected ResourceLocation BUTTON_IMAGE;


    private GuiButton buttonUnder;
    private long lastClick = 0;
    protected boolean pressed;
    public ButtonAdvanced(int buttonId,
                         int x,
                         int y,
                         int width,
                         int height,
                          ResourceLocation image,
                          GuiButton buttonUnder) {
        super(buttonId, x, y, width, height, "");
        this.BUTTON_IMAGE = image;
        this.buttonUnder = buttonUnder;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        pressed = false;
        if(System.currentTimeMillis()-lastClick>500) {
            lastClick = System.currentTimeMillis();
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        super.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(this.visible && isMouseOver() && (buttonUnder == null || !buttonUnder.isMouseOver())) {
            pressed = true;
            return true;
        }
        return false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            int x = this.x;
            int y = this.y;
            mc.getTextureManager().bindTexture(BUTTON_IMAGE);
            this.hovered = (buttonUnder == null || !buttonUnder.isMouseOver()) && mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
            //GL color
            if(pressed){
                GlStateManager.color(1.5F, 1.5F, 1.5F);
            }
            else if(hovered) {
                GlStateManager.color(0.9f, 0.9F, 0.9F);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F);
            }
            RenderUtils.drawCompleteImage(x, y, width, height, -1);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    public static Builder builder(int buttonId) {
        return new Builder(buttonId);
    }

    //Builder
    public static class Builder {
        private int buttonId;
        private int x;
        private int y;
        private int width;
        private int height;
        private ResourceLocation image;
        private GuiButton buttonUnder;
        public Builder(int buttonId) {
            this.buttonId = buttonId;
        }
        public Builder x(int x) {
            this.x = x;
            return this;
        }
        public Builder x(int x, int padding) {
            this.x = x+padding;
            return this;
        }
        public Builder y(int y) {
            this.y = y;
            return this;
        }
        public Builder y(int y, int padding) {
            this.y = y+padding;
            return this;
        }
        public Builder width(int width) {
            this.width = width;
            return this;
        }
        public Builder height(int height) {
            this.height = height;
            return this;
        }
        public Builder image(ResourceLocation image) {
            this.image = image;
            return this;
        }
        public Builder buttonUnder(GuiButton button) {
            this.buttonUnder = button;
            return this;
        }
        public ButtonAdvanced build() {
            return new ButtonAdvanced(buttonId, x, y, width, height, image, buttonUnder);
        }
    }
}
