package com.grandtheftwarzone.core.emoji.gui;

import com.grandtheftwarzone.core.emoji.GTWEmoji;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {

    /**
     * If the NBT tag should be hashed with MD5 when constructing the file name, and if an auxiliary txt file should be created with the full tag contents.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the NBT tag should be hashed with MD5 when constructing the file name, and if an auxiliary txt file should be created with the full tag contents.", isCommandable = true)
    public static boolean fileNameHashTag = false;

    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(GTWEmoji._instance, true, "general", null, GeneralConfig.class);
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
