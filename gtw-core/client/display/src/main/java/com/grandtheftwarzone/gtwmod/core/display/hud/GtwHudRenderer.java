package com.grandtheftwarzone.gtwmod.core.display.hud;


import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@SideOnly(Side.CLIENT)
public class GtwHudRenderer {
    boolean init = false;


    public GtwHudRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    public void applyHud(){
        String hud_id = Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().
                        getConfigManager().getConfig("settings"))
                .getString("hud");
        DisplayCanvas canvas =  GtwAPI.getInstance().getGtwMod().getDisplayManager()
                .getElementRegistry().getDrawableCanvas(
                        hud_id
                );
        if(canvas == null){
            throw new RuntimeException("HUD canvas not found - "+ hud_id);
        }
        GtwAPI.getInstance().getGtwMod().getDisplayManager()
                .setHUDCanvas(canvas);
    }

    @SubscribeEvent
    public void onGameOverlayRender(RenderGameOverlayEvent event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        switch(type) {
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
        if(!init){
            applyHud();
            init = true;
        }
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


}