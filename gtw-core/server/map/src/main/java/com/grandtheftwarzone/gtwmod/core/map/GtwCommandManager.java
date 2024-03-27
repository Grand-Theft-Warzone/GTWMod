package com.grandtheftwarzone.gtwmod.core.map;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GtwCommandManager extends CommandBase {
    @Override
    public String getName() {
        return "mapmanager";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mapmanager <reload|help>";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (!PermissionAPI.hasPermission(Objects.requireNonNull(server.getPlayerList().getPlayerByUsername(sender.getName())), "gtwmod.map.command.reload")) {
            return false;
        }
        return true;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> tab = new ArrayList<>();
        tab.add("reload");
        tab.add("help");
        return tab;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString("Usage: " + getUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(new TextComponentString("§eMap being reloaded..."));

            GtwAPI.getInstance().getMapManagerServer().initConfig();
            GameProfile[] playerList = GtwAPI.getInstance().getServer().getOnlinePlayerProfiles();
            for (GameProfile profile : playerList) {
                ProcessConsumer.getStartData(profile.getId());
            }

            sender.sendMessage(new TextComponentString("§aMap config reloaded!"));

        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(new TextComponentString("Help for Map Manager command..."));

        } else {
            sender.sendMessage(new TextComponentString("Unknown command. Usage: " + getUsage(sender)));
        }
    }


}
