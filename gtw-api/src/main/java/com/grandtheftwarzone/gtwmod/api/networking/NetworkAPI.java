package com.grandtheftwarzone.gtwmod.api.networking;

import com.grandtheftwarzone.gtwmod.api.map.data.CStartData;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.api.player.NotificationRequest;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface NetworkAPI {

    @NotNull
    NetworkManager getAtumNetwork();

    /**
     * Send player data to the client.
     *
     * @param pd The player data to send
     * @param playerUUID The player to send it to
     */
    @SideOnly(Side.SERVER)
    void sendPlayerData(@NotNull PlayerData pd,
                        @NotNull UUID playerUUID);

    /**
     * Send a notification to the client.
     *
     * @param notification The notification to send
     * @param playerUUID The player to send it to
     */
    @SideOnly(Side.SERVER)
    void sendNotification(@NotNull NotificationRequest notification,
                          @NotNull UUID playerUUID);




    void sendSRequest(Config config);

    void sendTest(String config, EntityPlayerMP player);

    void sendTestServer(String config);

    void sendConnect(EntityPlayerMP player);

    void sendMapStartData(CStartData cStartData, EntityPlayerMP player);

    void sendMapMarkers(List<TemplateMarker> cMarkers, EntityPlayerMP player);
}
