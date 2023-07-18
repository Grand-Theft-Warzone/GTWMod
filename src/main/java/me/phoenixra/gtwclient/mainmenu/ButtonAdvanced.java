package me.phoenixra.gtwclient.mainmenu;

import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

public class ButtonAdvanced extends GuiButton {

    private Function<Float,Integer> functionX;
    private Function<Float,Integer> functionY;

    private Function<Float,Integer> functionWidth;
    private Function<Float,Integer> functionHeight;


    protected ResourceLocation BUTTON_IMAGE;


    private GuiButton buttonUnder;
    private long lastClick = 0;
    protected boolean pressed;
    public ButtonAdvanced(int buttonId,
                          Function<Float,Integer> x,
                          Function<Float,Integer> y,
                          Function<Float,Integer> width,
                          Function<Float,Integer> height,
                          ResourceLocation image,
                          GuiButton buttonUnder) {
        super(buttonId, x.apply(2f), y.apply(2f), width.apply(2f),  height.apply(2f), "");
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
            GL11.glPushMatrix();
            float windowRationX = (float) RenderUtils.getWindowRatioWidth();
            float windowRationY = (float) RenderUtils.getWindowRatioHeight();
            int scaleFactor = RenderUtils.getScaleFactor();
            x = this.functionX.apply(scaleFactor/windowRationX);
            y = this.functionY.apply(scaleFactor/windowRationY);
            width = this.functionWidth.apply(scaleFactor/windowRationX);
            height = this.functionHeight.apply(scaleFactor/windowRationY);

            mc.getTextureManager().bindTexture(BUTTON_IMAGE);
            this.hovered = (buttonUnder == null || !buttonUnder.isMouseOver()) && mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
            //GL color
            if(pressed){
                GlStateManager.color(1.8F, 1.8F, 1.8F);
            }
            else if(hovered) {
                GlStateManager.color(0.9f, 0.9F, 0.9F);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F);
            }
            RenderUtils.drawCompleteImage(x, y, width, height, -1);
            this.mouseDragged(mc, mouseX, mouseY);
            GL11.glPopMatrix();
        }
    }

    public static Builder builder(int buttonId) {
        return new Builder(buttonId);
    }

    //Builder
    public static class Builder {
        private int buttonId;
        private Function<Float,Integer> x;
        private Function<Float,Integer> y;
        private Function<Float,Integer> width;
        private Function<Float,Integer> height;
        private ResourceLocation image;
        private GuiButton buttonUnder;
        public Builder(int buttonId) {
            this.buttonId = buttonId;
        }
        public Builder x(Function<Float,Integer> x) {
            this.x = x;
            return this;
        }
        public Builder y(Function<Float,Integer> y) {
            this.y = y;
            return this;
        }
        public Builder width(Function<Float,Integer> width) {
            this.width = width;
            return this;
        }
        public Builder height(Function<Float,Integer> height) {
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
