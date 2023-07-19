package me.phoenixra.libs.atum;

import me.phoenixra.libs.atum.input.KeyboardHandler;
import me.phoenixra.libs.atum.sound.SoundHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AtumMod {
    public static Logger LOGGER = LogManager.getLogger();

    public static boolean isOptifineLoaded = false;
    public AtumMod(){
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {

           // PopupHandler.init();

            KeyboardHandler.init();

            SoundHandler.init();

            //AdvancedButtonHandler.init();

            try {
                Class.forName("optifine.Installer");
                isOptifineLoaded = true;
                LOGGER.info("[AtumMod] Optifine detected! ###############################");
            }
            catch (ClassNotFoundException e) {}

        }
    }

    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {

            //MouseInput.init();

            LOGGER.info("[GtwClient] Client-side libs ready to use!");

            //PostLoadingHandler.runPostLoadingEvents();

        }

    }
}
