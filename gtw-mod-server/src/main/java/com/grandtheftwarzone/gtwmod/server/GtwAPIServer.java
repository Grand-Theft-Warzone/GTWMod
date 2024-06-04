package com.grandtheftwarzone.gtwmod.server;

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

public class GtwAPIServer implements GtwAPI {
    @Override
    public @NotNull PlayerData getPlayerData() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull NetworkAPI getNetworkAPI() {
        return GTWModServer.instance.getNetworkAPI();
    }

    @Override
    public @NotNull SoundsManager getSoundsManager() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull ScreensManager getScreensManager() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull PhoneManager getPhoneManager() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull MinecraftServer getServer() {
        return GTWModServer.instance.getServer();
    }

    @Override
    public @NotNull MapManagerClient getMapManagerClient() {
        return null;
    }

    @Override
    public @NotNull MapManagerServer getMapManagerServer() {
        return GTWModServer.instance.getMap();
    }

    @Override
    public @NotNull File getMinecraftDir() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull HashMap<UUID, Long> getGangsterMap() {
        return GTWModServer.instance.getGangsterMap();
    }

    @Override
    public void setGangsterMap(HashMap<UUID, Long> var) {
        GTWModServer.instance.setGangsterMap(var);
    }


    @Override
    public @NotNull AtumMod getGtwMod() {
        return GTWModServer.instance;
    }
}
