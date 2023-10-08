package com.grandtheftwarzone.gtwclient.api.event;

public interface GtwEvent {

    void setCancelled(boolean cancelled);
    boolean isCancelled();
}
