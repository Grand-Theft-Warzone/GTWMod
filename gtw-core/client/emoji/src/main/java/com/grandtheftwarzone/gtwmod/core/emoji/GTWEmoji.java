package com.grandtheftwarzone.gtwmod.core.emoji;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.collect.Lists;
import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.core.emoji.api.Emoji;
import com.grandtheftwarzone.gtwmod.core.emoji.render.EmojiFontRenderer;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.GtwProperties;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GTWEmoji implements AtumModService {

    public static final List<Emoji> EMOJI_LIST = new ArrayList<>();

    public static String GITHUB_URL = "https://raw.githubusercontent.com/Grand-Theft-Warzone/.github/main/emoji/";
    static boolean error = false;
    static boolean emoji_full = false;

    public static File minecraftDir;


    public GTWEmoji() {
        GtwLog.info("Main method Emoji is called.");
        GtwAPI.getInstance().getGtwMod().provideModService(
                this
        );
    }

    public static List<Emoji> readCategory(String cat) throws YamlException {
        GtwLog.info("Reading category: " + cat);

        String yamlContent = readStringFromURL(GITHUB_URL + cat);
        GtwLog.debug("Received YAML content:\n" + yamlContent);

        YamlReader categoryReader = new YamlReader(new StringReader(yamlContent));

        try {
            Emoji[] emojis = categoryReader.read(Emoji[].class);
            GtwLog.info("Successfully read emojis from category: " + cat);
            return Lists.newArrayList(emojis);
        } catch (YamlException e) {
            GtwLog.error("Error reading YAML content for category " + cat + ": " + e.getMessage());
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
            GtwLog.error("Error reading from URL: " + e);
        }
        return "";
    }


    public void generateEmojiList() {
        if (error) {
            GtwLog.error("Emoji List is not generated. See the error above.");
            return;
        }
        if (emoji_full) {
            GtwLog.error("The Emoji List is already fully formed. Skip regeneration.");
            return;
        }
        try {
            GtwLog.info("Generate Emoji List start.");

            EMOJI_LIST.clear();
            YamlReader reader = new YamlReader(new StringReader(readStringFromURL(GITHUB_URL + "Categories.yml")));
            ArrayList<String> categories = (ArrayList<String>) reader.read();
            for (String category : categories) {
                List<Emoji> emojis = readCategory(category);
                EMOJI_LIST.addAll(emojis);
            }
            emoji_full = true;
        } catch (YamlException e) {
            GtwLog.info("YAML Exception: " + e);
            GtwLog.info("Error!");
            error = true;
        } catch (NullPointerException e) {
            GtwLog.error("-> NullPointerException: " + e.getMessage());
            GtwLog.error("-> Error loading Emoji List. Please check your internet connection.");
            GtwLog.error("Details about the error: ");
            e.printStackTrace();
            //error = true;
        } catch (Exception e) {
            GtwLog.error("An unknown error has occurred: " + e.getMessage());
            error = true;
            e.printStackTrace();
        }
    }



    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
        if(fmlEvent instanceof FMLPreInitializationEvent){
            onPreInit((FMLPreInitializationEvent) fmlEvent);
        }else if(fmlEvent instanceof FMLInitializationEvent){
            onInit((FMLInitializationEvent)fmlEvent);
        }
    }
    public void onPreInit(FMLPreInitializationEvent event) {
        minecraftDir = event.getModConfigurationDirectory().getParentFile();
        GtwLog.info("Pre-Initialization Event is called. (Emoji)");
    }

    public void onInit(FMLInitializationEvent event) {
        generateEmojiList();
        ConfigManager.sync(GtwProperties.MOD_ID, Config.Type.INSTANCE);

        // Log information about the method being called
        GtwLog.info("Initialization Event is called. (Emoji)");

        if (!error) {
            Minecraft.getMinecraft().fontRenderer = new EmojiFontRenderer(Minecraft.getMinecraft());
        }
    }
    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "emoji";
    }
}