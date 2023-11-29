package com.grandtheftwarzone.gtwclient.core.minimap.packets;

import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.Collection;

public class PacketMarkerData implements IMessage {
    @Getter private ArrayList<Marker> markers;

    public PacketMarkerData() {
    }

    public PacketMarkerData(Collection<Marker> markers) {
        this.markers = new ArrayList<>(markers);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();

        markers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            markers.add(new Marker(buf.readInt(), buf.readInt(), Marker.MarkerType.values()[buf.readInt()]));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(markers.size());
        for (Marker marker : markers) {
            buf.writeInt(marker.getPosX());
            buf.writeInt(marker.getPosZ());
            buf.writeInt(marker.getType().ordinal());
        }
    }
}
