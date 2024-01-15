package com.grandtheftwarzone.gtwmod.server.proxy;

import com.grandtheftwarzone.gtwmod.api.event.EntityDamagedEvent;

import com.grandtheftwarzone.gtwmod.core.network.GtwNetworkAPI;
import com.grandtheftwarzone.gtwmod.server.GTWModServer;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommonProxy implements AtumModService {

    private List<Consumer<EntityDamagedEvent>> eventDamageObserver = new ArrayList<>();
    public CommonProxy(){
        MinecraftForge.EVENT_BUS.register(this);

        GTWModServer.instance.setNetworkAPI(new GtwNetworkAPI());
    }

    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {

    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onEntityDamaged(LivingDamageEvent event){
        if(event.getSource().getTrueSource() == null ||
        event.getSource().getImmediateSource() == null) return;

        EntityDamagedEvent e = new EntityDamagedEvent(
                event.getEntityLiving().getUniqueID(),
                event.getSource().getTrueSource().getUniqueID(),
                event.getSource().getImmediateSource().getUniqueID(),
                event.getAmount(),
                event.isCanceled()
        );
        eventDamageObserver.forEach(listener-> {
            listener.accept(e);
        });
        if (e.isCancelled()) {
            event.setCanceled(true);
        }else {
            event.setAmount(e.getDamage());
        }
    }
    @SideOnly(Side.SERVER)
    public void registerEventDamageListener(Consumer<EntityDamagedEvent> observer){
        eventDamageObserver.add(observer);
    }
    @SideOnly(Side.SERVER)
    public void unregisterEventDamageListener(Consumer<EntityDamagedEvent> observer){
        eventDamageObserver.remove(observer);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "proxy";
    }
}
