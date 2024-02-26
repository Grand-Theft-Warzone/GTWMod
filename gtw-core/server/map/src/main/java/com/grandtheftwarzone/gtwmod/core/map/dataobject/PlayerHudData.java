package com.grandtheftwarzone.gtwmod.core.map.dataobject;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerHudData {
    private UUID uuid;
    private int zoom;
    private boolean active;
}
