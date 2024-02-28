package com.grandtheftwarzone.gtwmod.api.map.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.phoenixra.atumconfig.api.config.Config;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SRequest {

    private UUID uuid;
    private Config config;

}
