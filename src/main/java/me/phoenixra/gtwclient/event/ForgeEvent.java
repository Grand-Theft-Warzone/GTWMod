package me.phoenixra.gtwclient.event;

public interface ForgeEvent {

    void setCancelled(boolean cancelled);
    boolean isCancelled();
}
