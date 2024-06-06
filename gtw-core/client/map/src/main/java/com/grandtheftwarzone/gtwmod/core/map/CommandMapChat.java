package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.BaseStaticMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
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
                showListMarker();
                mc.player.sendMessage(new TextComponentString("\n§8[LMM] §aMarker §f" + marker.getName() + "§a removed!"));
            }
            return;
        }

        // Create marker

        if (args.get(1).equalsIgnoreCase("create")) {
            if (args.size() == 2) {
                String msg = "§8[LMM] §fSyntax: §a/lmm create <name> <iconId> §7[X Y Z]";
                mc.player.sendMessage(new TextComponentString(msg));
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

            String msg = "§f" + i + ". §2" + marker.getName() + " §7(" + (int)location.getX() + " " + (int)location.getY() + " " + (int)location.getZ() + ")  " + "§c[§lX§c]";

            TextComponentString clickableMsg = new TextComponentString(msg);

            Style clickableStyle = new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commands.get(0) +" remove " + marker.getIdentificator())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("§cRemove marker")));
            clickableMsg.setStyle(clickableStyle);
            mc.player.sendMessage(clickableMsg);
        }
        mc.player.sendMessage(new TextComponentString("§7--======================--"));

    }

}
