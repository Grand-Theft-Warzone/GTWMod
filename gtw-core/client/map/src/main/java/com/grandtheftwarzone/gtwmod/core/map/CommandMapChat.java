package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.*;
import com.grandtheftwarzone.gtwmod.api.map.marker.impl.RadarClient;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandMapChat {

    public final Minecraft mc = Minecraft.getMinecraft();
    private final List<String> commands = new ArrayList<>();

    public CommandMapChat() {
        MinecraftForge.EVENT_BUS.register(this);

        commands.add("/lmapmanager");
        commands.add("/lmm");
    }


    @SubscribeEvent
    public void onPlayerChast(ClientChatEvent event) {
        String message = event.getMessage();
        boolean zash = false;

        if (message.replace(" ", "").equalsIgnoreCase("/mapmanagercreate")) {
            GtwAPI.getInstance().getMapManagerClient().setMarkerCreator(new MarkerCreationStateMachine(null, true));
            event.setCanceled(true);
            return;
        }

        List<String> args = Arrays.asList(message.split(" "));


        for (String command : commands) {
            if (args.get(0).toLowerCase().equals(command)) {
                zash = true;
            }
        }
        if (!zash) {
            return;
        }
        event.setCanceled(true);

        // HELP

        if (args.size() == 1 || args.get(1).equalsIgnoreCase("help")) {
            String msg = "§b---== §aAll commands: §b==---\n" +
                    "§d• §a/lmm help §f- Show all commands\n\n" +
                    "§d• §a/lmm create <name> <iconId> §7[X Y Z] §f- Create a local marker\n\n" +
                    "§d• §a/lmm remove §f- Remove local marker\n" +
                    "§b---=======================---";

            mc.player.sendMessage(new TextComponentString(msg));
            return;
        }


        // REMOVE MARKER

        if (args.size() == 3 && args.get(1).equalsIgnoreCase("remove")) {

            String identificator = args.get(2);

            MapMarker marker = GtwAPI.getInstance().getMapManagerClient().getMarkerManager().removeLocalMarker(identificator);
            if (marker == null) {
                mc.player.sendMessage(new TextComponentString("§8[LMM] §cToken with id §f" + identificator + "§c was not found!"));
            } else {
//                showListMarker();
                mc.player.sendMessage(new TextComponentString("\n§8[LMM] §aMarker §f" + marker.getName() + "§a removed!"));
            }
            return;
        }

        // Create marker

        if (args.get(1).equalsIgnoreCase("create")) {
            if (args.size() == 2) {
                GtwAPI.getInstance().getMapManagerClient().setMarkerCreator(new MarkerCreationStateMachine(null, false));
                return;
            }
            if (args.size() >= 4) {
                String nameMarker = args.get(2);
                String iconId = args.get(3);
                EntityLocation location;
                if (args.size() == 7) {
                    try {
                        location = new EntityLocation(Double.parseDouble(args.get(4)), Double.parseDouble(args.get(6)), Double.parseDouble(args.get(5)));
                    } catch (NumberFormatException e) {
                        String msg = "§8[LMM] §cERROR:Check the correctness of the arguments.";
                        mc.player.sendMessage(new TextComponentString(msg));
                        throw new RuntimeException(e);
                    }
                } else {
                    location = new EntityLocation(mc.player.posX, mc.player.posZ, mc.player.posY, mc.player.cameraYaw, mc.player.cameraPitch);
                }

                List<String> mapImageId = new ArrayList<>();
                mapImageId.add(GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinimapImage().getImageId());

                TemplateMarker templateMarker = new TemplateMarker(nameMarker, null, iconId, location.toString(), null, true, mapImageId, null, true);

                GtwAPI.getInstance().getMapManagerClient().getMarkerManager().addLocalMarker(new BaseStaticMarker(templateMarker));

                String msg = "§8[LMM] §aMarker §f" + nameMarker + "§a successfully created! §7[Show markers]";
                TextComponentString clickableMsg = new TextComponentString(msg);
                Style clickableStyle = new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commands.get(0) +" list"));
                clickableMsg.setStyle(clickableStyle);
                mc.player.sendMessage(clickableMsg);
                return;
            }

        }

        if (args.get(1).equalsIgnoreCase("edit")) {
            if (args.size() == 3) {
                String msgStr = "§e[GTWMap] §fSyntax: §a/lmm edit <id> name/lore/iconid/worldlocation/data/mapimageid/permissions/actions/draw <newVar> §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/lmm list", "§8Show markers");
                mc.player.sendMessage(msg);
                return;
            }
            if (args.size() == 2) {
                String msgStr = "§e[GTWMap] §fThere is no required command argument. §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/lmm list", "§8Show markers");
                mc.player.sendMessage(msg);
                return;
            }

            String identificator = args.get(2);
            BaseStaticMarker marker = (BaseStaticMarker) GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getLocalMarker(identificator);
            if (marker == null) {
                String msgStr = "§e[GTWMap] §cToken with id §f" + identificator + "§c was not found!";
                mc.player.sendMessage(new TextComponentString(msgStr));
            } else {

                String action = args.get(3);
                List<String> newVar = args.subList(4, args.size()-1);
                if (action.equalsIgnoreCase("name")) {
                    marker.setName(String.join(" ", newVar));
                } else if (action.equalsIgnoreCase("lore")) {
                    marker.setLore(String.join(" ", newVar));
                } else if (action.equalsIgnoreCase("iconid")) {
                    marker.setIconId(String.join(" ", newVar));
                } else if (action.equalsIgnoreCase("worldlocation")) {
                    marker.setWorldLocation(String.join(" ", newVar));
                } else  if (action.equalsIgnoreCase("draw")) {
                    marker.setDraw(Boolean.parseBoolean(String.join(" ", newVar)));
                }
                GtwAPI.getInstance().getMapManagerClient().getMarkerManager().addLocalMarker(marker);

                mc.player.sendChatMessage("/lmm info " + identificator);

                TextComponentString msg = getClicableMsg("§e[LMM] §aParameter " + action + " changed! §7[Edit]", ClickEvent.Action.SUGGEST_COMMAND, "/lmm edit " + action + " " + newVar, "§7Change this parameter again");
                mc.player.sendMessage(msg);
                return;
            }

        }

        // LIST

        if (args.get(1).equalsIgnoreCase("remove") || args.get(1).equalsIgnoreCase("list")) {
            showListMarker();
            mc.player.sendMessage(new TextComponentString("\n§8[LMM] §4<!> Click on the marker you want to delete."));
            return;
        }

    }

    private void showListMarker() {
        List<MapMarker> markerList = GtwAPI.getInstance().getMapManagerClient().getMarkerManager().getLocalMarkerList();


        mc.player.sendMessage(new TextComponentString("\n§7--=== §aLocal Marker List §7===--"));

        int i = 0;
        if (markerList.isEmpty()) {
            mc.player.sendMessage(new TextComponentString("\n§fit's empty!\n"));
        }
        for (MapMarker marker : markerList) {
            i++;
            EntityLocation location = marker.getWorldLocation();

            if (marker instanceof RadarClient) {
                String msg = "§f" + i + ". §2" + marker.getName() + " §7(" + (int)location.getX() + " " + (int)location.getY() + " " + (int)location.getZ() + ")";

                TextComponentString clickableMsg = new TextComponentString(msg);
                mc.player.sendMessage(clickableMsg);
            } else {
                String msg = "§f" + i + ". §2" + marker.getName() + " §7(" + (int)location.getX() + " " + (int)location.getY() + " " + (int)location.getZ() + ")  " + "§c[§lX§c]";

                TextComponentString clickableMsg = new TextComponentString(msg);

                Style clickableStyle = new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commands.get(0) +" remove " + marker.getIdentificator())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("§cRemove marker")));
                clickableMsg.setStyle(clickableStyle);
                mc.player.sendMessage(clickableMsg);

            }
        }
        mc.player.sendMessage(new TextComponentString("§7--======================--"));

    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, HoverEvent.Action eventHover, String argHover) {
        TextComponentString text = new TextComponentString(textMsg);
        text.setStyle(new Style().setClickEvent(new ClickEvent(eventClick, argClick)).setHoverEvent(new HoverEvent(eventHover, new TextComponentString(argHover))));
        return text;
    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, String argHover) {
        return this.getClicableMsg(StringUtils.formatMinecraftColors(textMsg), eventClick, argClick, HoverEvent.Action.SHOW_TEXT, StringUtils.formatMinecraftColors(argHover));
    }

}
