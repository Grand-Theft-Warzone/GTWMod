package com.grandtheftwarzone.gtwmod.server;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneGui;

import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

public class GtwAPIServer implements GtwAPI {
    @Override
    public @NotNull PlayerData getPlayerData() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull NetworkAPI getNetworkManager() {
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
    public @NotNull FactoryGuiHandler getFactoryGuiHandler() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull PhoneGui getPhoneGui() {
        throw new RuntimeException("Client side only!");
    }

    @Override
    public @NotNull AtumMod getGtwMod() {
        return GTWModServer.instance;
    }
}
