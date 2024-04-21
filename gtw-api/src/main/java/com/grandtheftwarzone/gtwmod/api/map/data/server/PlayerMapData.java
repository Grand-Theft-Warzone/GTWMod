package com.grandtheftwarzone.gtwmod.api.map.data.server;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerMapData {

    private UUID uuid;
    @Setter
    @Nullable
    private String minimapId;
    @Setter
    @Nullable
    private String globalmapId;
    private boolean allowMapDisplay;

    public String getMinimapId() {
        if (minimapId == null || minimapId.equals("default")) {
            return GtwAPI.getInstance().getMapManagerServer().getDefaultMinimapId();
        }
        return this.minimapId;
    }

    public String getGlobalmapId() {
        if (globalmapId == null || globalmapId.equals("default")) {
            return GtwAPI.getInstance().getMapManagerServer().getDefaultGlobalmapId();
        }
        return this.globalmapId;
    }

}
