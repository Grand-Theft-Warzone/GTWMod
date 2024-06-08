package com.grandtheftwarzone.gtwmod.core.network.impl.map.packet;

import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.map.data.CStartData;
import com.grandtheftwarzone.gtwmod.api.map.data.MapImageData;
import com.grandtheftwarzone.gtwmod.api.map.data.RestrictionsData;
import com.grandtheftwarzone.gtwmod.api.misc.MapLocation;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PacketMapStartData implements IMessage {

    private MapImageData minimapData;
    private MapImageData globalmapData;
    private RestrictionsData restrictionsData;

    public PacketMapStartData() {
        this.minimapData = new MapImageData();
        this.globalmapData = new MapImageData();

    }

    public PacketMapStartData(CStartData cStartData) {
        this.minimapData = cStartData.getMinimapData();
        this.globalmapData = cStartData.getGlobalmapData();
        this.restrictionsData = cStartData.getRestrictionsData();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        GtwLog.getLogger().debug("Called toBytes " + getClass().getSimpleName());
        byte[] bytes;
        MapImageData[] maps = {minimapData, globalmapData};


        for (MapImageData mapData : maps) {
            // Записываем imageId
            if (mapData == null) {
                GtwLog.getLogger().debug("=================\nCard details " + "null" +
                                "\nПусто..." +
                                "================="
                        );
                buf.writeBoolean(false);
            } else {
                GtwLog.getLogger().debug("=================\nCard details " + mapData.getClass().getSimpleName() +
                        "\nimageId: " + mapData.getImageId() +
                        "\ncord: " + mapData.getTopRight() + "  " + mapData.getDownRight() + "  " + mapData.getDownLeft() + "  " + mapData.getTopLeft() +
                        "\nOffset: " + mapData.getOffsetX() + " " + mapData.getOffsetY() +
                        "\ncolor фон: " + (mapData.getColorBackground() != null ? mapData.getColorBackground().toString() : "Нету") +
                        "\ncolor барьер: " + (mapData.getColorBorderReach() != null ? mapData.getColorBorderReach().toString() : "Нету") +
                        "\n================="
                );
                buf.writeBoolean(true);
                bytes = mapData.getImageId().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
                // Записываем координаты
                bytes = mapData.getTopRight().toString().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);

                bytes = mapData.getDownRight().toString().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);

                bytes = mapData.getDownLeft().toString().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);

                bytes = mapData.getTopLeft().toString().getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);

                buf.writeInt(mapData.getOffsetX());
                buf.writeInt(mapData.getOffsetY());

                // Цвет фона
                String colorBackground;
                if (mapData.getColorBackground() == null) {
                    colorBackground = "null";
                } else {
                    colorBackground = mapData.getColorBackground().toHex(true);
                }
                bytes = colorBackground.getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);

                // Цвет границы досигаемости.
                String colorBorderReach;
                if (mapData.getColorBorderReach() == null) {
                    colorBorderReach = "null";
                } else {
                    colorBorderReach = mapData.getColorBorderReach().toHex(true);
                }
                bytes = colorBorderReach.getBytes(StandardCharsets.UTF_8);
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }

        }

        GtwLog.getLogger().debug(
                "Restrictions: " +
                "\nAllow Display: " + restrictionsData.isAllowMapDisplay() +
                "\nAllow Local Marker: " + restrictionsData.isAllowLocalMarker() +
                "\nMin Zoom: " + restrictionsData.getMinZoom() +
                "\nMax Zoom: " + restrictionsData.getMaxZoom()
        );

        // Ограничения
        bytes = restrictionsData.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        buf.writeBoolean(restrictionsData.isAllowMapDisplay());
        buf.writeBoolean(restrictionsData.isAllowLocalMarker());
        buf.writeInt(restrictionsData.getMinZoom());
        buf.writeInt(restrictionsData.getMaxZoom());

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        GtwLog.getLogger().debug("Called fromBytes " + getClass().getSimpleName());
        byte[] bytes;

        MapImageData[] maps = {this.minimapData, this.globalmapData};
        for (int i = 0; i < maps.length; i++) {
            MapImageData mapData;

            boolean isNull = !buf.readBoolean();
            if (isNull) {
                mapData = null;
                GtwLog.getLogger().debug(
                        "\n------------\n Card details " + null +
                        "\nit's empty... \n------------"
                );
            } else {
                // imageId
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                String imageId = new String(bytes, StandardCharsets.UTF_8);

                // Координаты
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                MapLocation topRight = new MapLocation(new String(bytes, StandardCharsets.UTF_8));


                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                MapLocation downRight = new MapLocation(new String(bytes, StandardCharsets.UTF_8));


                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                MapLocation downLeft = new MapLocation(new String(bytes, StandardCharsets.UTF_8));


                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                MapLocation topLeft = new MapLocation(new String(bytes, StandardCharsets.UTF_8));

                int offsetX = buf.readInt();
                int offsetY = buf.readInt();

                // Цвет фона
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                AtumColor colorBackground;
                String colorBackgroundText = new String(bytes, StandardCharsets.UTF_8);
                if (colorBackgroundText.equals("null")) {
                    colorBackground = null;
                } else {
                    colorBackground = AtumColor.fromHex(colorBackgroundText);
                }

                // Цвет границы достигаемости.
                bytes = new byte[buf.readInt()];
                buf.readBytes(bytes);
                AtumColor colorBorderReach;
                String colorBorderReachText = new String(bytes, StandardCharsets.UTF_8);
                if (colorBorderReachText.equals("null")) {
                    colorBorderReach = null;
                } else {
                    colorBorderReach = AtumColor.fromHex(colorBorderReachText);
                }


                mapData = new MapImageData(imageId, topRight, downRight, downLeft, topLeft, offsetX, offsetY, colorBackground, colorBorderReach);

                GtwLog.getLogger().debug(
                        "\n------------ " +
                                "\nCard details " + mapData.getClass().getCanonicalName() +
                                "\nimageId: " + mapData.getImageId() +
                                "\ncord: " + mapData.getTopRight() + "  " + mapData.getDownRight() + "  " + mapData.getDownLeft() + "  " + mapData.getTopLeft() +
                                "\noffset: " + mapData.getOffsetX() + "  " + mapData.getOffsetY() +
                                "\ncolor background: " + (mapData.getColorBackground() != null ? mapData.getColorBackground().toString() : "нету") +
                                "\ncolor barrier: " + (mapData.getColorBorderReach() != null ? mapData.getColorBorderReach().toString() : "нету") + "\n------------"
                );

            }

            if (i == 0) {
                this.minimapData = mapData;
            }
            if (i == 1) {
                this.globalmapData = mapData;
            }

        }

        // Ограничения
        bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        UUID uuid = UUID.fromString(new String(bytes, StandardCharsets.UTF_8));
        boolean allowMapDisplay = buf.readBoolean();
        boolean allowLocalMarker = buf.readBoolean();
        int minZoom = buf.readInt();
        int maxZoom = buf.readInt();

        this.restrictionsData = new RestrictionsData(uuid, allowMapDisplay, allowLocalMarker, minZoom, maxZoom);

        GtwLog.getLogger().debug(
                "\n-|- RESTRICTIONS: " +
                        "\nAllow Display: " + restrictionsData.isAllowMapDisplay() +
                        "\nAllow Local Marker: " + restrictionsData.isAllowLocalMarker() +
                        "\nMin Zoom: " + restrictionsData.getMinZoom() +
                        "\nMax Zoom: " + restrictionsData.getMaxZoom()
        );


    }

}
