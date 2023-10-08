package com.grandtheftwarzone.gtwclient.mod;

import com.grandtheftwarzone.gtwclient.api.GtwAPI;
import com.grandtheftwarzone.gtwclient.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneGui;
import com.grandtheftwarzone.gtwclient.api.hud.PlayerHUD;
import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwclient.api.sound.SoundsManager;
import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

public class GtwAPIImpl implements GtwAPI {
    @Override
    public @NotNull PlayerData getPlayerData() {
        return GTWClient.instance.getPlayerData();
    }

    @Override
    public @NotNull NetworkManager getNetworkManager() {
        return GTWClient.instance.getNetworkManager();
    }

    @Override
    public @NotNull PlayerHUD getPlayerHUD() {
        return GTWClient.instance.getPlayerHUD();
    }

    @Override
    public @NotNull SoundsManager getSoundsManager() {
        return GTWClient.instance.getSoundsManager();
    }

    @Override
    public @NotNull ScreensManager getScreensManager() {
        return GTWClient.instance.getScreensManager();
    }

    @Override
    public @NotNull FactoryGuiHandler getFactoryGuiHandler() {
        return GTWClient.instance.getFactoryGuiHandler();
    }

    @Override
    public @NotNull PhoneGui getPhoneGui() {
        return GTWClient.instance.getPhoneGui();
    }

    @Override
    public @NotNull AtumMod getGtwMod() {
        return GTWClient.instance;
    }
}
