package com.grandtheftwarzone.gtwmod.api;

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;


public class GtwLog {
    private static final String MOD_ID = GtwProperties.MOD_ID;

    public static void log(Level logLevel, String message) {
        FMLLog.log(MOD_ID, logLevel, message);
    }

    public static void debug(String message) {
        FMLLog.log(MOD_ID, Level.DEBUG, message);
    }

    public static void info(String message) {
        FMLLog.log(MOD_ID, Level.INFO, message);
    }

    public static void all(String message) {
        FMLLog.log(MOD_ID, Level.ALL, message);
    }

    public static void error(String message) {
        FMLLog.log(MOD_ID, Level.ERROR, message);
    }
}
