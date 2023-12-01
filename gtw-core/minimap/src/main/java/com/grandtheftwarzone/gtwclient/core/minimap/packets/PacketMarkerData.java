package com.grandtheftwarzone.gtwclient.core.minimap.packets;

import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.MarkerType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PacketMarkerData implements IMessage {
    private ArrayList<Marker> markers;

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();

        markers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int posX = buf.readInt();
            int posZ = buf.readInt();

            String texturePath = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
            String textureDomain = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
            boolean global = buf.readBoolean();
            int textureX = buf.readInt();
            int textureY = buf.readInt();
            int textureWidth = buf.readInt();
            int textureHeight = buf.readInt();

            markers.add(new Marker(posX, posZ,
                    new MarkerType(
                            new ResourceLocation(textureDomain, texturePath),
                            global, textureX, textureY, textureWidth, textureHeight))
            );
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(markers.size());
        for (Marker marker : markers) {
            buf.writeInt(marker.getPosX());
            buf.writeInt(marker.getPosZ());

            //TODO: Check if this is the correct way to send the marker texture
            String texturePath = marker.getType().getTexture().getResourcePath();
            String textureDomain = marker.getType().getTexture().getResourceDomain();

            buf.writeInt(texturePath.length());
            buf.writeCharSequence(texturePath, StandardCharsets.UTF_8);
            buf.writeInt(textureDomain.length());
            buf.writeCharSequence(textureDomain, StandardCharsets.UTF_8);

            buf.writeBoolean(marker.getType().isGlobal());
            buf.writeInt(marker.getType().getTextureX());
            buf.writeInt(marker.getType().getTextureY());
            buf.writeInt(marker.getType().getTextureWidth());
            buf.writeInt(marker.getType().getTextureHeight());
        }
    }
}
