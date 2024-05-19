package com.grandtheftwarzone.gtwmod.core.network.impl.map.packet;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.ConfigType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PacketMapMarkers implements IMessage {

    private List<TemplateMarker> markers;

    @Override
    public void fromBytes(ByteBuf buf) {
        System.out.print("Вызывается fromBytes " + getClass().getSimpleName());
        byte[] bytes;
        boolean notNull;

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {

            bytes = new byte[buf.readInt()];
            buf.readBytes(bytes);
            String id = new String(bytes, StandardCharsets.UTF_8);

            String name;
            notNull = buf.readBoolean();
            if (notNull) {
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                name = new String(bytes, StandardCharsets.UTF_8);
            } else {
                name = null;
            }

            String lore;
            notNull = buf.readBoolean();
            if (notNull) {
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                lore = new String(bytes, StandardCharsets.UTF_8);
            } else {
                lore = null;
            }

            bytes = new byte[buf.readInt()];
            buf.readBytes(bytes);
            String iconId = new String(bytes, StandardCharsets.UTF_8);

            bytes = new byte[buf.readInt()];
            buf.readBytes(bytes);
            String worldLocation = new String(bytes, StandardCharsets.UTF_8);

            notNull = buf.readBoolean();
            Config data;
            if (notNull) {
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                String configText = new String(bytes, StandardCharsets.UTF_8);
                data = GtwAPI.getInstance().getGtwMod().getConfigManager().createConfigFromString(configText, ConfigType.YAML);
            } else {
                data = null;
            }

            boolean localMarker = buf.readBoolean();

            notNull = buf.readBoolean();
            List<String> mapImageIds;
            if (notNull) {
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                String mapImageIdsString = new String(bytes, StandardCharsets.UTF_8);
                mapImageIds = TemplateMarker.getMapImageIdsFromString(mapImageIdsString);
            } else {
                mapImageIds = null;
            }

            notNull = buf.readBoolean();
            List<String> actionList;
            if (notNull) {
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                String actionListString = new String(bytes, StandardCharsets.UTF_8);
                actionList = TemplateMarker.getActionListFromString(actionListString);
            } else {
                actionList = null;
            }

            boolean draw = buf.readBoolean();

            TemplateMarker marker = new TemplateMarker(id, name, lore, iconId, worldLocation, data, localMarker, mapImageIds, actionList, draw);
            System.out.print("=================================" +
                    "\nДанные маркера " + marker.getName() + ": " +
                    "\n\nID: " + marker.getIdentificator() +
                    "\nName: " + marker.getName() +
                    "\nLore: " + marker.getLore() +
                    "\nIcon ID: " + iconId +
                    "\nWL: " + marker.getWorldLocation() +
                    "\nData: " + marker.getData() +
                    "\nLocal: " + marker.isLocalMarker() +
                    "\nMap Images: " + marker.getMapImageIds() +
                    "\nActions: " + marker.getActionList() +
                    "\nDraw: " + marker.isDraw() +
                    "=================================") ;
            markers.add(marker);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        System.out.println("Вызывается toBytes " + getClass().getSimpleName());
        byte[] bytes;

        buf.writeInt(markers.size());

        for (TemplateMarker marker : markers) {
            // ID
            bytes = marker.getIdentificator().getBytes(StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);

            // Name
            if (marker.getName() == null || marker.getName().isEmpty()) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                bytes = marker.getName().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }

            // lore
            if (marker.getLore() == null || marker.getLore().isEmpty()) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                bytes = marker.getLore().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }

            // iconId
            bytes = marker.getIconId().getBytes(StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);

            // worldLocation
            bytes = marker.getWorldLocation().getBytes(StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);

            // data
            if (marker.getData() == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                bytes = marker.getData().toPlaintext().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }

            // localMarker
            buf.writeBoolean(marker.isLocalMarker());

            // mapImageIds
            if (marker.getMapImageIds() == null || marker.getMapImageIds().isEmpty()) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                bytes = Objects.requireNonNull(marker.getMapImageIdsString()).getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }

            // actionList
            if (marker.getActionList() == null || marker.getActionList().isEmpty()) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                bytes = Objects.requireNonNull(marker.getActionListString()).getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }

            // draw
            buf.writeBoolean(marker.isDraw());

        }

    }
}
