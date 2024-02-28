package com.grandtheftwarzone.gtwmod.core.map.dataobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RestrictionsData {
    private UUID uuid;
    private boolean allowMapDisplay;
    private int minZoom;
    private int maxZoom;
}
