package com.grandtheftwarzone.gtwmod.api.map.marker;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ServerMarker extends TemplateMarker {

    @Getter @Nullable
    private List<String> permissions;

    public ServerMarker(String identificator, @Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable String data, boolean localMarker, @Nullable String mapImageIds, @Nullable String permissions, @Nullable String actionList, boolean draw) {
        super(identificator, name, lore, iconId, worldLocation, data, localMarker, mapImageIds, actionList, draw);
        this.permissions = (permissions != null && !permissions.isEmpty() && !permissions.equals("null"))? Arrays.asList(permissions.split(";")) : null;
    }

    public ServerMarker(@Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable String data, boolean localMarker, @Nullable String mapImageIds, @Nullable String permissions, @Nullable String actionList, boolean draw) {
        super(name, lore, iconId, worldLocation, data, localMarker, mapImageIds, actionList, draw);
        this.permissions = (permissions != null && !permissions.isEmpty() && !permissions.equals("null"))? Arrays.asList(permissions.split(";")) : null;
    }

    public ServerMarker(@Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable String data, @Nullable String mapImageIds, @Nullable String permissions, @Nullable String actionList, boolean draw) {
        super(name, lore, iconId, worldLocation, data, false, mapImageIds, actionList, draw);
        this.permissions = (permissions != null && !permissions.isEmpty() && !permissions.equals("null"))? Arrays.asList(permissions.split(";")) : null;
    }

    public ServerMarker(String identificator, @Nullable String name, @Nullable String lore, String iconId, String worldLocation, @Nullable String data, @Nullable String mapImageIds, @Nullable String permissions, @Nullable String actionList, boolean draw) {
        super(identificator, name, lore, iconId, worldLocation, data, false, mapImageIds, actionList, draw);
        this.permissions = (permissions != null && !permissions.isEmpty() && !permissions.equals("null"))? Arrays.asList(permissions.split(";")) : null;
    }

    public void updateData(ServerMarker updateMarkerData) {
        super.updateData(updateMarkerData);
        this.permissions = updateMarkerData.getPermissions();
    }

    public @Nullable String getPermissionsString() {
        return (permissions!= null &&!permissions.isEmpty())? String.join(";", permissions) : null;
    }

    public void setPermissions(String permissions) {
        this.permissions = (permissions != null && !permissions.isEmpty() && !permissions.equals("null")) ? Arrays.asList(permissions.split(";")) : null;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

}
