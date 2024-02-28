package com.grandtheftwarzone.gtwmod.core.map.dataobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerData {

    private UUID uuid;
    @Setter
    private String minimapId;
    @Setter
    private String globalId;
    private boolean allowMapDisplay;

}
