package com.grandtheftwarzone.gtwmod.api.utils;

import me.phoenixra.atumodcore.api.utils.PlayerUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GtwUtils {
    public static String getCurrentTime() {
        if(PlayerUtils.isInMultiplayer()){
           //@TODO implement server time
        }
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

}
