package com.grandtheftwarzone.gtwmod.core.map.dataobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerData {

    private UUID uuid;
    private String minimapId;
    private String globalId;
    private boolean allowMapDisplay;

}
