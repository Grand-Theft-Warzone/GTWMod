package com.grandtheftwarzone.gtwmod.core.display.hud.render;


import com.grandtheftwarzone.gtwmod.core.display.GtwPlayerHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GtwHudRenderer {

    private Minecraft mc;

    public GtwHudRenderer() {
        this.mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onGameOverlayRender(RenderGameOverlayEvent event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        switch(type) {
            case ALL:
                if(event instanceof RenderGameOverlayEvent.Pre) break;
                renderOverlay();
                break;
            case FOOD:
            case HEALTH:
            case EXPERIENCE:
            case ARMOR:
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
            case ARMOR:
            case POTION_ICONS:
                event.setCanceled(true);
                break;
            default:
                break;

        }
    }


    private void renderOverlay() {
        ScaledResolution res = new ScaledResolution(mc);
        GtwPlayerHUD.instance.draw(this.mc.ingameGUI,res.getScaledWidth(),res.getScaledHeight());
    }
}