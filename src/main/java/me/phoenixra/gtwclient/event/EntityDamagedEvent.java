package me.phoenixra.gtwclient.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@AllArgsConstructor
public class EntityDamagedEvent implements ForgeEvent {
    @Getter
    private UUID entity;

    @Getter
    private UUID damager;
    @Getter
    private UUID realDamager;

    @Getter @Setter
    private float damage;
    @Getter @Setter
    private boolean isCancelled;

}
