package com.grandtheftwarzone.gtwmod.core.misc;

import com.grandtheftwarzone.gtwmod.api.GtwProperties;
import net.minecraftforge.common.config.Config;

@Config(modid = GtwProperties.MOD_ID)
public class GTWConfig {

    @Config.Name("Display Emoji")
    @Config.Comment("Enable or disable rendering emoji")
    public static boolean render_emoji = true;
}
