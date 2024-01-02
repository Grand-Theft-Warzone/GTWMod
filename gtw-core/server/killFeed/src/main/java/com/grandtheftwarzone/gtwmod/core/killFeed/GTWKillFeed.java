package com.grandtheftwarzone.gtwmod.core.killFeed;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class GTWKillFeed {

    private Map<String, Map<String, String>> mods;

    private Map<String, String> regexp;

    private List<PairRecord<String, String>> variables;

    private Config config;

    public static Boolean debug;


    public GTWKillFeed() {
        GtwLog.info("The killFeed module is starting...");

        MinecraftForge.EVENT_BUS.register(this);

    }


    public void initConfig() {
        config = AtumAPI.getInstance().createLoadableConfig(GtwAPI.getInstance().getGtwMod(), "config", "killFeed", ConfigType.YAML, true);

        debug = config.getBool("debug");
        if (debug) GtwLog.info("[DEBUG killFeed] Start init config");


        variables = new ArrayList<>();
        if (debug) GtwLog.info("======================\n[DEBUG killFeed] Variables list:");
        Config variableSection = config.getSubsection("variable");
        for(String key : variableSection.getKeys(false)){
            variables.add(new PairRecord<>("$" + key, variableSection.getString(key)));
            if (debug) GtwLog.info("* " + key + " -|- " + variableSection.getString(key));
        }
        if (debug) GtwLog.info("======================");


        regexp = new HashMap<>();
        if (debug) GtwLog.info("[DEBUG killFeed] Regexp list:");
        Config regexpSection = config.getSubsection("regexp");
        for(String key : regexpSection.getKeys(false)){
            regexp.put(key, regexpSection.getString(key));
            if (debug) GtwLog.info("* " + key + " -|- " + regexpSection.getString(key));
        }
        if (debug) GtwLog.info("======================");

        if (debug) GtwLog.info("======================\n[DEBUG killFeed] Mods/Item list:");
        mods = new HashMap<>();
        Config modsSection = config.getSubsection("mods");
        for (String modName : modsSection.getKeys(false)) {
            Map<String, String> blablacar = new HashMap<>();
            Config cfg = modsSection.getSubsection(modName);
            if (debug) GtwLog.info("* " + modName + ":");
            for (String key : cfg.getKeys(false)) {
                blablacar.put(key, cfg.getString(key));
                if (debug) GtwLog.info("  - " + key + " -|- " + cfg.getString(key));
            }
            mods.put(modName, blablacar);
        }
        if (debug) GtwLog.info("======================");

    }



    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (debug) GtwLog.info("[killFeed] Caught the LivingDeathEvent event");
            if (player.getAttackingEntity() != null && !(player.getAttackingEntity() instanceof FakePlayer)) {
                EntityLivingBase attacker = player.getAttackingEntity();

                ItemStack weaponStack = attacker.getHeldItemMainhand();
                ResourceLocation itemRegistryName = weaponStack.getItem().getRegistryName();
                String weaponId = itemRegistryName != null ? itemRegistryName.toString() : "unknown";

                if (config.getBool("metadata") && weaponStack.getMetadata() != 0) {
                    weaponId += "__" + weaponStack.getMetadata();
                }

                String modName = weaponId.split(":")[0];
                String itemName = weaponId.split(":")[1];


                String killText = "";

                for (Map.Entry<String, Map<String, String>> itemEntry : mods.entrySet()) {
                    String currentModName = itemEntry.getKey();
                    if (currentModName.equals(modName)) {
                        Map<String, String> itemMap = itemEntry.getValue();

                        if (itemMap.containsKey(itemName.replace(".", "♪"))) {
                            killText = itemMap.get(itemName.replace(".", "♪"));
                            break;
                        }
                        if (itemMap.containsKey("other")) {
                            killText = itemMap.get("other");
                            break;
                        }
                    }

                }

                for (Map.Entry<String, String> regexpEntry : regexp.entrySet()) {
                    if (weaponId.matches(regexpEntry.getKey().replace("♪", "."))) {
                        killText = regexpEntry.getValue();
                        break;
                    }
                }

                String default_killText = config.getString("default_killText");
                if (killText.isEmpty() && !default_killText.isEmpty()) killText = default_killText;


                if (debug) {
                    String debugData = "\n====== Data kill ======" +
                            "\nKillerName: " + attacker.getName() +
                            "\nPlayerName: " + player.getName() +
                            "\nModName: " + modName +
                            "\nItem: " + itemName +
                            "\nMetaData: " + weaponStack.getMetadata() +
                            "\nMetaData (Boolean config): " + config.getBool("metadata") +
                            "\nFullItemName: " + weaponId +
                            "\nFullItemName (Config): " + weaponId.replace(".", "♪") +
                            "\n\nStatus: " + !killText.isEmpty() +
                            "\nkillText: " + killText +
                            "\n=======================";
                    GtwLog.info(debugData);
                }

                if (!killText.isEmpty()) {
                    List<PairRecord<String, String>> variablesData = new ArrayList<>();
                    variablesData.add(new PairRecord<>("$killer", attacker.getName()));
                    variablesData.add(new PairRecord<>("$player", player.getName()));
                    variablesData.add(new PairRecord<>("$mod_name", modName));
                    variablesData.add(new PairRecord<>("$item", itemName));
                    variablesData.add(new PairRecord<>("$full_item", weaponId));
                    killText = StringUtils.format(StringUtils.replaceFast(killText, variables));
                    killText = StringUtils.format(StringUtils.replaceFast(killText, variablesData));
                    TextComponentString message = new TextComponentString(killText);
                    PlayerList playerList = Objects.requireNonNull(player.getServer()).getPlayerList();
                    playerList.sendMessage(message);
                    event.setCanceled(true);
                }
            }
        }
    }
}
