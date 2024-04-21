package com.grandtheftwarzone.gtwmod.server.proxy;

import com.grandtheftwarzone.gtwmod.api.event.EntityDamagedEvent;

import com.grandtheftwarzone.gtwmod.core.network.GtwNetworkAPI;
import com.grandtheftwarzone.gtwmod.server.GTWModServer;
import me.phoenixra.atumodcore.api.events.network.PlayerDisplayEvent;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommonProxy implements AtumModService {

    private List<Consumer<EntityDamagedEvent>> eventDamageObserver = new ArrayList<>();

    private List<Consumer<PlayerDisplayEvent>> eventPlayerDisplayObserver = new ArrayList<>();
    public CommonProxy(){
        MinecraftForge.EVENT_BUS.register(this);

    }

    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {

    }

    @SubscribeEvent
    public void onDisplayEvent(PlayerDisplayEvent event){
        eventPlayerDisplayObserver.forEach(listener-> {
            listener.accept(event);
        });
    }
    @SubscribeEvent
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
    public void registerEventDamageListener(Consumer<EntityDamagedEvent> observer){
        eventDamageObserver.add(observer);
    }
    public void unregisterEventDamageListener(Consumer<EntityDamagedEvent> observer){
        eventDamageObserver.remove(observer);
    }

    public void registerPlayerDisplayListener(Consumer<PlayerDisplayEvent> observer){
        eventPlayerDisplayObserver.add(observer);
    }
    public void unregisterPlayerDisplayListener(Consumer<PlayerDisplayEvent> observer){
        eventPlayerDisplayObserver.remove(observer);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "proxy";
    }
}
