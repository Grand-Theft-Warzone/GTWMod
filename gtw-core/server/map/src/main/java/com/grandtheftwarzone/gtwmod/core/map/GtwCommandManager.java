package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.map.marker.MapMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.ServerMarker;
import com.grandtheftwarzone.gtwmod.api.map.marker.TemplateMarker;
import com.grandtheftwarzone.gtwmod.api.misc.EntityLocation;
import com.mojang.authlib.GameProfile;
import me.phoenixra.atumconfig.api.config.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.util.*;


public class GtwCommandManager extends CommandBase {
    @Override
    public String getName() {
        return "mapmanager";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mapmanager <reload|create|remove|help>";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (!(sender instanceof MinecraftServer) && server.getPlayerList().getPlayerByUsername(sender.getName()) != null && !PermissionAPI.hasPermission(Objects.requireNonNull(server.getPlayerList().getPlayerByUsername(sender.getName())), "gtwmod.map.command.*")) {
            return false;
        }
        return true;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (!checkPermission(server, sender)) {
            return null;
        }
        List<String> tab = new ArrayList<>();
        if (args.length == 0) {
            tab.add("reload");
            tab.add("create");
            tab.add("list");
            return tab;
        }

        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            String msg = "§b---== §aAll commands: §b==---\n" +
                    "§d• §a/" + getName() + " help §f- Show all commands\n\n" +
                    "§d• §a/" + getName() + " create <name> <iconId> §7[X Y Z] §f- Create a marker\n\n" +
                    "§d• §a/" + getName() + " list §f- Get a list of all markers\n\n" +
                    "§d• §a/" + getName() + " info <id> §f- Read/Edit маркер";


            sender.sendMessage(new TextComponentString(msg));
            return;
        }

        // RELOAD
        if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(new TextComponentString("§e[GTWMap] Map being reloaded..."));

            GtwAPI.getInstance().getMapManagerServer().initConfig();
            GtwAPI.getInstance().getMapManagerServer().getMarkerManager().initMarker();
            GameProfile[] playerList = GtwAPI.getInstance().getServer().getOnlinePlayerProfiles();
            for (GameProfile profile : playerList) {
                ProcessConsumer.getStartData(profile.getId());
            }

            sender.sendMessage(new TextComponentString("§e[GTWMap] §aMap config and marker reloaded!"));
            return;
        }


        //TEST
        if (args[0].equalsIgnoreCase("list")) {


            List<ServerMarker> markers = GtwAPI.getInstance().getMapManagerServer().getMarkerManager().getAllMarker();

            TextComponentString message = new TextComponentString("§b---====§a< All markers >§b====---\n");

            int pageSize = 10;
            int totalPages = (markers.size() + pageSize - 1) / pageSize;
            int page = 1;
            if (args.length >= 2) {
                page = Integer.parseInt(args[1]);
                if (page < 1) {
                    page = 1;
                } else if (page > totalPages) {
                    page = totalPages;
                }
            }


            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, markers.size());

            if (markers.isEmpty()) {
                message.appendSibling(getClicableMsg("§e\nNo markers found! §a[Create]", ClickEvent.Action.RUN_COMMAND, "/" + getName() + " create", "§8Create a marker"));
            }

            for (int i = startIndex; i < endIndex; i++) {
                ServerMarker marker = markers.get(i);
                TextComponentString markerMsg = new TextComponentString("\n§7" + (i+1) + ". ");

                EntityLocation posMarker = new EntityLocation(marker.getWorldLocation());
                TextComponentString margerName = getClicableMsg("§f" +(marker.getName() != null ? marker.getName() : "-") + " ", ClickEvent.Action.RUN_COMMAND, "/tp " + ((EntityPlayerMP)sender).getName() + " " + posMarker.getX() + " " + posMarker.getZ() + " " + posMarker.getY(), "§8" + (int)posMarker.getX() + " " + (int)posMarker.getZ() + " " + (int)posMarker.getY());
                markerMsg.appendSibling(margerName);

                TextComponentString markerEdit = getClicableMsg("§b[Edit] ", ClickEvent.Action.RUN_COMMAND ,"/" + getName() + " info " + marker.getIdentificator(), "§8Edit marker");
                markerMsg.appendSibling(markerEdit);

                TextComponentString markerRemove = getClicableMsg("§c[X] ", ClickEvent.Action.SUGGEST_COMMAND, "/" + getName() + " remove " + marker.getIdentificator(), "§8Remove marker");
                markerMsg.appendSibling(markerRemove);

                message.appendSibling(markerMsg);
            }

            TextComponentString endMessage = new TextComponentString("\n\n§b---====== ");
            TextComponentString back = getClicableMsg("<< ", ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list " + (page-1), "§8Previous page");
            endMessage.appendSibling(back);

            endMessage.appendSibling(new TextComponentString("§f" + page + "§a/§f" + totalPages));

            TextComponentString next = getClicableMsg(" >> ", ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list " + (page+1), "§8Next page");
            endMessage.appendSibling(next);
            endMessage.appendSibling(new TextComponentString("§b======---"));

            message.appendSibling(endMessage);

            sender.sendMessage(message);
            return;
        }

        // REMOVE
        if (args[0].equalsIgnoreCase("remove")) {

            if (args.length == 1) {
                String msgStr = "§e[GTWMap] §fSyntax: §a/" + getName() + " remove <id> §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list", "§8Show markers");
                sender.sendMessage(msg);
            }

            String identificator = args[1];

            ServerMarker marker = GtwAPI.getInstance().getMapManagerServer().getMarkerManager().removeMarker(identificator);
            if (marker == null) {
                String msgStr = "§e[GTWMap] §cToken with id §f" + identificator + "§c was not found! §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list", "§8Show markers");
                sender.sendMessage(msg);
            } else {
                String msgStr = "§e[GTWMap] §aMarker §f" + marker.getName() + "§a removed! §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list", "§8Show markers");
                sender.sendMessage(msg);
            }

            return;
        }

        // INFO
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                String msgStr = "§e[GTWMap] §fSyntax: §a/" + getName() + " info <id> §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list", "§8Show markers");
                sender.sendMessage(msg);
            }

            String identificator = args[1];

            ServerMarker marker = GtwAPI.getInstance().getMapManagerServer().getMarkerManager().getMarker(identificator);
            if (marker == null) {
                String msgStr = "§e[GTWMap] §cToken with id §f" + identificator + "§c was not found! §7[Show markers]";
                TextComponentString msg = getClicableMsg(msgStr, ClickEvent.Action.RUN_COMMAND, "/" + getName() + " list", "§8Show markers");
                sender.sendMessage(msg);
            } else {

                TextComponentString msg = new TextComponentString("§b---========§a<< " + marker.getName() + " §a>>§b========---\n");

                TextComponentString msgId = getClicableMsg("\n§7• §fID: §8" + marker.getIdentificator(), ClickEvent.Action.SUGGEST_COMMAND, marker.getIdentificator(), "§8Copy ID");
                msg.appendSibling(msgId);

                String cmdPrefix = "/" + getName() + " edit " + marker.getIdentificator() + " ";

                TextComponentString msgName = getClicableMsg("\n\n§b• §aName: §f" + marker.getName(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "name " + marker.getName(), "§8Change name");
                msg.appendSibling(msgName);

                TextComponentString msgLore = getClicableMsg("\n§b• §aLore: §f" + marker.getLore(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "lore " + marker.getLore(), "§8Change description");
                msg.appendSibling(msgLore);

                TextComponentString msgIconId = getClicableMsg("\n§d• §aIcon Id: §f" + marker.getIconId(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "iconid " + marker.getIconId(), "§8Change icon id");
                msg.appendSibling(msgIconId);

                EntityLocation worldLocation = new EntityLocation(marker.getWorldLocation());
                TextComponentString msgWorldLocation = getClicableMsg("\n§e• §aWorldLocation: §f" + (int)worldLocation.getX() + " " + (int)worldLocation.getZ() + " " + (int)worldLocation.getY(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "worldlocation " + marker.getWorldLocation(), "§8Change worldLocation");
                msg.appendSibling(msgWorldLocation);


                Config config = marker.getData();
                TextComponentString msgData = getClicableMsg("\n§e• §aConfig Data: §f" + (config != null ? config.toPlaintext() : "null"), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "data " + marker.getData(), "§8Change data");
                msg.appendSibling(msgData);

                TextComponentString msgMapImageId = getClicableMsg("\n§d• §aMap Image Binding: §f" + marker.getMapImageIds(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "mapimageid " + marker.getMapImageIdsString(), "§8Change map image binding");
                msg.appendSibling(msgMapImageId);

                TextComponentString msgPermissions = getClicableMsg("\n§d• §aPermissions: §f" + marker.getPermissions(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "permissions " + marker.getPermissionsString(), "§8Change permissions");
                msg.appendSibling(msgPermissions);

                TextComponentString msgAction = getClicableMsg("\n§d• §aAction: §f" + marker.getActionList(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "actions " + marker.getActionListString(), "§8Change actions");
                msg.appendSibling(msgAction);

                TextComponentString msgDraw = getClicableMsg("\n\n§f• §aDraw: §f" + marker.isDraw(), ClickEvent.Action.SUGGEST_COMMAND, cmdPrefix + "draw " + marker.isDraw(), "§8Change draw");
                msg.appendSibling(msgDraw);

                TextComponentString msgDelete = getClicableMsg("\n\n§e✘ §cDelete? §f§a[Yes]", ClickEvent.Action.SUGGEST_COMMAND,  "/" + getName() + " remove " + marker.getIdentificator(), "§eDelete marker");
                msg.appendSibling(msgDelete);

                TextComponentString msgEnd = new TextComponentString("\n\n        §7(Click to edit) \n§b---==========================---");
                msg.appendSibling(msgEnd);

                sender.sendMessage(msg);
                return;
            }


        }

        // CREATE
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 3) {
                String msgText = "§8[GTWMap] §fSyntax: §a/" + getName() + " create <name> <iconId> [permissions] §7[X Y Z]";
                TextComponentString msg = getClicableMsg(msgText, ClickEvent.Action.SUGGEST_COMMAND, "/" + getName() + " create", "§8Substitute command");
                sender.sendMessage(msg);
                return;
            }

            String name = args[1];
            String iconId = args[2];
            List<String> permissions = null;
            if (args.length > 3) {
                permissions = (args[3] != null && !args[3].isEmpty() && !args[3].equals("null"))? Arrays.asList(args[3].split(";")) : null;
            }
            String permissionsStr = (permissions!= null &&!permissions.isEmpty())? String.join(";", permissions) : null;

            EntityLocation location;
            if (args.length == 6) {
                try {
                    location = new EntityLocation(Double.parseDouble(args[3] != null ? args[3] : String.valueOf(0)), Double.parseDouble(args[5]), Double.parseDouble(args[4]));
                } catch (NumberFormatException e) {
                    String msg = "§e[GTWMap] §cERROR:Check the correctness of the arguments.";
                    sender.sendMessage(new TextComponentString(msg));
                    throw new RuntimeException(e);
                }
            } else {
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    location = new EntityLocation(player.posX, player.posZ, player.posY, player.cameraYaw, player.cameraPitch);
                } else {
                    location = new EntityLocation();
                }
            }


            List<String> mapImageId = new ArrayList<>();
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                mapImageId.add(GtwAPI.getInstance().getMapManagerServer().getPlayerData(player.getUniqueID()).getMinimapId());
            }
            String mapImageIdStr = !mapImageId.isEmpty() ? String.join(";", mapImageId) : null;

            ServerMarker newServerMarker = new ServerMarker(name, null, iconId, location.toString(), null, false, mapImageIdStr, permissionsStr, null, true);

            GtwAPI.getInstance().getMapManagerServer().getMarkerManager().createOrUpdateMarker(newServerMarker);
            TextComponentString msg = getClicableMsg("§e[GTWMap] §aMarker §f" + name + "§a has been created! §b[Edit]", ClickEvent.Action.RUN_COMMAND, "/" + getName() + " info " + newServerMarker.getIdentificator(), "§8Edit marker");

            sender.sendMessage(msg);
            return;
        }




        sender.sendMessage(new TextComponentString("Unknown command. Usage: " + getUsage(sender)));

    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, HoverEvent.Action eventHover, String argHover) {
        TextComponentString text = new TextComponentString(textMsg);
        text.setStyle(new Style().setClickEvent(new ClickEvent(eventClick, argClick)).setHoverEvent(new HoverEvent(eventHover, new TextComponentString(argHover))));
        return text;
    }

    public TextComponentString getClicableMsg(String textMsg, ClickEvent.Action eventClick, String argClick, String argHover) {
        return this.getClicableMsg(textMsg, eventClick, argClick, HoverEvent.Action.SHOW_TEXT, argHover);
    }

}
