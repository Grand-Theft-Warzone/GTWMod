package com.grandtheftwarzone.gtwclient.core.minimap.packets;

import com.grandtheftwarzone.gtwclient.core.minimap.markers.Marker;
import com.grandtheftwarzone.gtwclient.core.minimap.markers.MarkerType;
import com.grandtheftwarzone.gtwclient.core.minimap.utils.TextureAtlas;
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

            //TODO: Find a way to tell the client which marker to use (they should already been created)
            String texturePath = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
            String textureDomain = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
            boolean global = buf.readBoolean();
            int textureWidth = buf.readInt();
            int textureHeight = buf.readInt();
            int atlasWidth = buf.readInt();
            int atlasHeight = buf.readInt();
            int atlasIndex = buf.readInt();


            markers.add(new Marker(posX, posZ,
                    new MarkerType(
                            new ResourceLocation(textureDomain, texturePath),
                            new TextureAtlas(atlasIndex, textureWidth, textureHeight, atlasWidth, atlasHeight), global))
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
            buf.writeInt(marker.getType().getAtlas().getTextureWidth());
            buf.writeInt(marker.getType().getAtlas().getTextureHeight());
            buf.writeInt(marker.getType().getAtlas().getWidth());
            buf.writeInt(marker.getType().getAtlas().getHeight());
            buf.writeInt(marker.getType().getAtlas().getIndex());
        }
    }
}
