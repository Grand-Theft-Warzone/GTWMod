package com.grandtheftwarzone.gtwmod.client;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;
import com.grandtheftwarzone.gtwmod.api.map.manager.client.MapManagerClient;
import com.grandtheftwarzone.gtwmod.api.map.manager.server.MapManagerServer;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import me.phoenixra.atumodcore.api.AtumMod;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class GtwAPIClient implements GtwAPI {
    @Override
    public @NotNull PlayerData getPlayerData() {
        return GTWModClient.instance.getPlayerData();
    }

    @Override
    public @NotNull NetworkAPI getNetworkAPI() {
        return GTWModClient.instance.getNetworkAPI();
    }

    @Override
    public @NotNull SoundsManager getSoundsManager() {
        return GTWModClient.instance.getSoundsManager();
    }

    @Override
    public @NotNull ScreensManager getScreensManager() {
        return GTWModClient.instance.getScreensManager();
    }


    @Override
    public @NotNull PhoneManager getPhoneManager() {
        return GTWModClient.instance.getPhoneManager();
    }

    @Override
    public @NotNull MinecraftServer getServer() {
        throw new RuntimeException("Server side only!");
    }

    @Override
    public @NotNull MapManagerClient getMapManagerClient() {
        return GTWModClient.instance.getMap();
    }

    @Override
    public @NotNull MapManagerServer getMapManagerServer() {
        throw new RuntimeException("Server side only!");
    }

    @Override
    public @NotNull AtumMod getGtwMod() {
        return GTWModClient.instance;
    }

    @Override
    public @NotNull File getMinecraftDir() {
        return GTWModClient.instance.getMinecraftDir();
    }

    @Override
    public @NotNull HashMap<UUID, Long> getGangsterMap() {
        throw new RuntimeException("Server side only!");
    }

    @Override
    public void setGangsterMap(HashMap<UUID, Long> var) {
        throw new RuntimeException("Server side only!");
    }


}
