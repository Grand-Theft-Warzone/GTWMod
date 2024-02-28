package com.grandtheftwarzone.gtwmod.api;

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
    @NotNull NetworkAPI getNetworkAPI();



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
    @NotNull PhoneManager getPhoneManager();

    @SideOnly(Side.SERVER)
    @NotNull MinecraftServer getServer();

    @SideOnly(Side.CLIENT)
    @NotNull MapManagerClient getMapManagerClient();

    @SideOnly(Side.SERVER)
    @NotNull MapManagerServer getMapManagerServer();

    /**
     * Get GTW mod instance
     *
     * @return The atum mod
     */
    @NotNull AtumMod getGtwMod();



    static @NotNull String getGtwAsciiArt(){
        return "\n\n" +
                " ██████╗     ████████╗    ██╗    ██╗\n" +
                "██╔════╝     ╚══██╔══╝    ██║    ██║\n" +
                "██║  ███╗       ██║       ██║ █╗ ██║\n" +
                "██║   ██║       ██║       ██║███╗██║\n" +
                "╚██████╔╝       ██║       ╚███╔███╔╝\n" +
                " ╚═════╝        ╚═╝        ╚══╝╚══╝ " +
                "\n                                 --- Grand Theft Warzone project\n";
    }




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
