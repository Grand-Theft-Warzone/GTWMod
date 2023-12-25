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

import javax.swing.*;


public class GTWEmoji {

    private static final Logger LOGGER = Logger.getLogger(GTWEmoji.class.getName());

    public static final List<Emoji> EMOJI_LIST = new ArrayList<>();

    public static String GITHUB_URL = "https://raw.githubusercontent.com/Grand-Theft-Warzone/.github/main/emoji/";
    static boolean error = false;

    public static File minecraftDir;

    @Mod.Instance(value = Reference.MOD_ID)
    public static ModBase _instance;


    public GTWEmoji() {
        System.out.println("Main method is called.");
        generateEmojiList();
    }

    public static List<Emoji> readCategory(String cat) throws YamlException {
        System.out.println("Reading category: " + cat);

        String yamlContent = readStringFromURL(GITHUB_URL + cat);
        System.out.println("Received YAML content:\n" + yamlContent);

        YamlReader categoryReader = new YamlReader(new StringReader(yamlContent));

        try {
            Emoji[] emojis = categoryReader.read(Emoji[].class);
            System.out.println("Successfully read emojis from category: " + cat);
            return Lists.newArrayList(emojis);
        } catch (YamlException e) {
            System.err.println("Error reading YAML content for category " + cat + ": " + e.getMessage());
            throw e;
        }
    }
    public static String readStringFromURL(String requestURL) {
        try {
            try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                    StandardCharsets.UTF_8.toString())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (IOException e) {
            // Log the exceptionz
            LOGGER.log(Level.SEVERE, "Error reading from URL", e);
        }
        return "";
    }


    private static void generateEmojiList() {

        try {
            LOGGER.log(Level.INFO, "Generate Emoji List start.");

            YamlReader reader = new YamlReader(new StringReader(readStringFromURL(GITHUB_URL + "Categories.yml")));
            ArrayList<String> categories = (ArrayList<String>) reader.read();
            for (String category : categories) {
                List<Emoji> emojis = readCategory(category);
                EMOJI_LIST.addAll(emojis);
            }
        } catch (YamlException e) {
            System.out.println("YAML Exception "+ e);
            System.out.println("Error!");
            error = true;
        }
    }



    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        minecraftDir = event.getModConfigurationDirectory().getParentFile();

        generateEmojiList();
//        MinecraftForge.EVENT_BUS.register(this);
        ConfigManager.sync(GtwProperties.MOD_ID, Config.Type.INSTANCE);
    }


    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        // Log information about the method being called
        System.out.println("Initialization Event is called.");

        if (!error) {
            Minecraft.getMinecraft().fontRenderer = new EmojiFontRenderer(Minecraft.getMinecraft());
        }
    }




}