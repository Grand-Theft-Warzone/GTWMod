package com.grandtheftwarzone.gtwmod.core.map.dataobject;

import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StaticMarker {

    private Integer id;
    private String name;
    private String lore;
    private String mapId;
    private MapLocation cord;
    private String iconId;
    private String[] permission;

    public String getPermissionOfString() {
        StringBuilder permissions = new StringBuilder();
        for (String perm : this.permission) {
            permissions.append(perm).append(";");
        }
        return permissions.toString();
    }
}
