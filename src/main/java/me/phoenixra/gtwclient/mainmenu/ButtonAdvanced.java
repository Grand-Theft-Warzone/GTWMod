package me.phoenixra.gtwclient.mainmenu;

import lombok.Setter;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class ButtonAdvanced extends GuiButton {

    private Function<Integer,Integer> functionX;
    private Function<Integer,Integer> functionY;

    private Function<Integer,Integer> functionWidth;
    private Function<Integer,Integer> functionHeight;


    protected ResourceLocation BUTTON_IMAGE;


    private GuiButton buttonUnder;
    private long lastClick = 0;
    protected boolean pressed;
    public ButtonAdvanced(int buttonId,
                          Function<Integer,Integer> x,
                          Function<Integer,Integer> y,
                          Function<Integer,Integer> width,
                          Function<Integer,Integer> height,
                          ResourceLocation image,
                          GuiButton buttonUnder) {
        super(buttonId, x.apply(2), y.apply(2), width.apply(2), height.apply(2), "");
        this.BUTTON_IMAGE = image;
        this.buttonUnder = buttonUnder;
        functionX = x;
        functionY = y;
        functionWidth = width;
        functionHeight = height;
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
            int scaleFactor = RenderUtils.getScaleFactor();
            x = this.functionX.apply(scaleFactor);
            y = this.functionY.apply(scaleFactor);
            width = this.functionWidth.apply(scaleFactor);
            height = this.functionHeight.apply(scaleFactor);
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
        private Function<Integer,Integer> x;
        private Function<Integer,Integer> y;
        private Function<Integer,Integer> width;
        private Function<Integer,Integer> height;
        private ResourceLocation image;
        private GuiButton buttonUnder;
        public Builder(int buttonId) {
            this.buttonId = buttonId;
        }
        public Builder x(Function<Integer,Integer> x) {
            this.x = x;
            return this;
        }
        public Builder y(Function<Integer,Integer> y) {
            this.y = y;
            return this;
        }
        public Builder width(Function<Integer,Integer> width) {
            this.width = width;
            return this;
        }
        public Builder height(Function<Integer,Integer> height) {
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
