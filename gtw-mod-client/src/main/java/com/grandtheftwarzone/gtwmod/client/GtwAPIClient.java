package com.grandtheftwarzone.gtwmod.client;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneGui;
import com.grandtheftwarzone.gtwmod.api.networking.NetworkAPI;
import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwmod.api.sound.SoundsManager;
import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

public class GtwAPIClient implements GtwAPI {
    @Override
    public @NotNull PlayerData getPlayerData() {
        return GTWModClient.instance.getPlayerData();
    }

    @Override
    public @NotNull NetworkAPI getNetworkManager() {
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
    public @NotNull PhoneGui getPhoneGui() {
        return GTWModClient.instance.getPhoneGui();
    }

    @Override
    public @NotNull AtumMod getGtwMod() {
        return GTWModClient.instance;
    }
}
