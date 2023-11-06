package com.grandtheftwarzone.gtwclient.api;

import com.grandtheftwarzone.gtwclient.api.gui.FactoryGuiHandler;
import com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneGui;
import com.grandtheftwarzone.gtwclient.api.networking.NetworkManager;
import com.grandtheftwarzone.gtwclient.api.hud.PlayerHUD;
import com.grandtheftwarzone.gtwclient.api.player.PlayerData;
import com.grandtheftwarzone.gtwclient.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwclient.api.sound.SoundsManager;
import me.phoenixra.atumodcore.api.AtumMod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public interface GtwAPI {

    /**
     * Get the player data of a client
     *
     * <p>CLIENT SIDE!</p>
     *
     * @return The player data
     */
    @SideOnly(Side.CLIENT)
    @NotNull PlayerData getPlayerData();

    /**
     * Get the network manager
     *
     * @return The network manager
     */
    @NotNull NetworkManager getNetworkManager();

    /**
     * Get the player hud handler
     *
     * <p>CLIENT SIDE!</p>
     *
     * @return The player hud
     */
    @SideOnly(Side.CLIENT)
    @NotNull PlayerHUD getPlayerHUD();

    /**
     * Get the sounds manager
     *
     * @return The sounds manager
     */
    @NotNull SoundsManager getSoundsManager();

    /**
     * Get the custom screens manager
     *
     * <p>CLIENT SIDE!</p>
     *
     * @return The custom screens manager
     */
    @SideOnly(Side.CLIENT)
    @NotNull ScreensManager getScreensManager();

    /**
     * Get the factory gui handler
     *
     * <p>CLIENT SIDE!</p>
     *
     * @return The factory gui handler
     */
    @SideOnly(Side.CLIENT)
    @NotNull FactoryGuiHandler getFactoryGuiHandler();

    /**
     * Get the phone gui
     *
     * <p>CLIENT SIDE!</p>
     *
     * @return The phone gui
     */
    @SideOnly(Side.CLIENT)
    @NotNull PhoneGui getPhoneGui();

    /**
     * Get GTW mod instance
     *
     * @return The atum mod
     */
    @NotNull AtumMod getGtwMod();






    static GtwAPI getInstance() {
        return Instance.get();
    }

    final class Instance {
        private static GtwAPI api;
        private Instance() {
            throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
        }

        public static void set(final GtwAPI api) {
            if(Instance.api != null) return;

            Instance.api = api;
        }


        static GtwAPI get() {
            return api;
        }
    }

}