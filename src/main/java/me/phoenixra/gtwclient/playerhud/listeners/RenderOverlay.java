package me.phoenixra.gtwclient.playerhud.listeners;


import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderOverlay {

    private Minecraft mc;

    public RenderOverlay() {
        this.mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onGameOverlayRender(RenderGameOverlayEvent event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        switch(type) {
            case ALL:
                if(event instanceof RenderGameOverlayEvent.Pre) break;
                renderOverlay(event.getPartialTicks());
                break;
            case FOOD:
            case HEALTH:
            case EXPERIENCE:
            case POTION_ICONS:
                event.setCanceled(true);
                break;
            default:
                break;

        }
    }

    @SubscribeEvent
    public void onGameOverlayRender(RenderGameOverlayEvent.Pre event) {
        switch(event.getType()) {
            case FOOD:
            case HEALTH:
            case EXPERIENCE:
            case POTION_ICONS:
                event.setCanceled(true);
                break;
            default:
                break;

        }
    }


    private void renderOverlay(float partialTicks) {
        this.drawElement(HudElementType.WIDGET, partialTicks);
        this.drawElement(HudElementType.HEALTH, partialTicks);
        this.drawElement(HudElementType.FOOD, partialTicks);
        this.drawElement(HudElementType.EXPERIENCE, partialTicks);
        this.drawElement(HudElementType.LEVEL, partialTicks);
        this.drawElement(HudElementType.MONEY, partialTicks);
        this.drawElement(HudElementType.RANK, partialTicks);
        this.drawElement(HudElementType.NOTIFICATION,partialTicks);
    }

    /**
     * Draw the specified HudElement of the HudElementType from the active Hud
     *
     * @param type         the HudElementType to be rendered
     * @param partialTicks the partialTicks to be used for animations
     */
    private void drawElement(HudElementType type, float partialTicks) {

        ScaledResolution res = new ScaledResolution(mc);
        bind(Gui.ICONS);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Hud.instance.drawElement(type, this.mc.ingameGUI, partialTicks, partialTicks, res.getScaledWidth(),
                res.getScaledHeight());
        GlStateManager.popMatrix();
    }


    private void bind(ResourceLocation res) {
        mc.getTextureManager().bindTexture(res);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiResize(GuiScreenEvent.InitGuiEvent event) {

       /* if(!mc.isFullScreen()) {
            mc.toggleFullscreen();
        }*/

    }
}