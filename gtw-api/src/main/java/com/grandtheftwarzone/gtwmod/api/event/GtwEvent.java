package com.grandtheftwarzone.gtwmod.api.event;

public interface GtwEvent {

    void setCancelled(boolean cancelled);
    boolean isCancelled();
}
