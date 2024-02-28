package com.grandtheftwarzone.gtwmod.client;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;
import com.grandtheftwarzone.gtwmod.api.map.MapManagerClient;
import com.grandtheftwarzone.gtwmod.api.map.MapManagerServer;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import me.phoenixra.atumodcore.api.AtumMod;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull FactoryGuiHandler getFactoryGuiHandler() {
        return GTWModClient.instance.getFactoryGuiHandler();
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



}
