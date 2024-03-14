package com.grandtheftwarzone.gtwmod.api.map.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RestrictionsData {
    private UUID uuid;
    private boolean allowMapDisplay;
    private boolean allowLocalMarker;
    private int minZoom;
    private int maxZoom;
}
