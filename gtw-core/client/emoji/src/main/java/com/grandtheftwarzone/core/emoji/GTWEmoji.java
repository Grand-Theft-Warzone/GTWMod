package com.grandtheftwarzone.core.emoji;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.collect.Lists;
import com.grandtheftwarzone.core.emoji.api.Emoji;
import com.grandtheftwarzone.core.emoji.render.EmojiFontRenderer;
import com.grandtheftwarzone.gtwmod.api.GtwProperties;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.init.ModBase;


public class GTWEmoji {

    private static final Logger LOGGER = Logger.getLogger(GTWEmoji.class.getName());

    public static final Map<String, List<Emoji>> EMOJI_MAP = new HashMap<>();
    public static final List<Emoji> EMOJI_LIST = new ArrayList<>();

    public static String GITHUB_URL = "https://raw.githubusercontent.com/Grand-Theft-Warzone/.github/main/emoji/";
    static boolean error = false;

    public static File minecraftDir;

    @Mod.Instance(value = Reference.MOD_ID)
    public static ModBase _instance;


    public GTWEmoji() {
        LOGGER.log(Level.INFO, "Main method is called.");
        MinecraftForge.EVENT_BUS.register(this);
        generateEmojiList();
    }

    public static List<Emoji> readCategory(String cat) throws YamlException {
        LOGGER.log(Level.INFO, "Reading category: {0}", cat);

        YamlReader categoryReader = new YamlReader(new StringReader(readStringFromURL(GITHUB_URL + cat)));
        return Lists.newArrayList(categoryReader.read(Emoji[].class));
    }

    public static String readStringFromURL(String requestURL) {
        try {
            try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                    StandardCharsets.UTF_8.toString())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (IOException e) {
            // Log the exception
            LOGGER.log(Level.SEVERE, "Error reading from URL", e);
        }
        return "";
    }


    private static void generateEmojiList() {

        try {
            LOGGER.log(Level.INFO, "Pre-Initialization Event is called.");

            YamlReader reader = new YamlReader(new StringReader(readStringFromURL(GITHUB_URL + "Categories.yml")));
            ArrayList<String> categories = (ArrayList<String>) reader.read();
            for (String category : categories) {
                List<Emoji> emojis = readCategory(category);
                EMOJI_LIST.addAll(emojis);
                EMOJI_MAP.put(category, emojis);
            }
        } catch (YamlException e) {
            LOGGER.log(Level.SEVERE, "YAML Exception", e);
            LOGGER.log(Level.SEVERE, "Error!");
            error = true;
        }
    }



    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        File minecraftDir = event.getModConfigurationDirectory().getParentFile();

        generateEmojiList();
        MinecraftForge.EVENT_BUS.register(this);
        ConfigManager.sync(GtwProperties.MOD_ID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(GtwProperties.MOD_ID)) {
            ConfigManager.sync(GtwProperties.MOD_ID, Config.Type.INSTANCE);
        }
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        // Log information about the method being called
        LOGGER.log(Level.INFO, "Initialization Event is called.");

        if (!error) {
            Minecraft.getMinecraft().fontRenderer = new EmojiFontRenderer(Minecraft.getMinecraft());
        }
    }




}

//package com.grandtheftwarzone.gtwemoji;
//
//        import net.minecraftforge.common.config.Config;
//
//@Config(modid = GTWEmoji.MODID,name = "GTWConfig")
//public class GTWEmojiConfig {
//
//    @Config.Name("emoji_render")
//    @Config.LangKey("config.gtwemoji.emoji_render")
//    public static boolean renderEmoji = false;
//
//}
