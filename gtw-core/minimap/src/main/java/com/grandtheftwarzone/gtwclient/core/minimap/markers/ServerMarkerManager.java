package com.grandtheftwarzone.gtwclient.core.minimap.markers;

import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.core.minimap.packets.PacketMarkerData;
import lombok.AllArgsConstructor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.UUID;

@SideOnly(Side.SERVER)
@AllArgsConstructor
public class ServerMarkerManager {
    private final NetworkManager n;

    private final ArrayList<Marker> markers = new ArrayList<>();

    public void insert(Marker marker) {
        markers.add(marker);
    }

    public void remove(Marker marker) {
        markers.remove(marker);
    }

    public void sync() {
        n.sendToAll(new PacketMarkerData(markers));
    }

    public void sync(UUID uuid) {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);

        n.sendTo(new PacketMarkerData(markers), player);
    }

}
