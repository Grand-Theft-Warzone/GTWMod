package com.grandtheftwarzone.gtwmod.core.map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandMapChat {

    public final Minecraft mc = Minecraft.getMinecraft();
    private final List<String> commands = new ArrayList<>();

    public CommandMapChat() {
        MinecraftForge.EVENT_BUS.register(this);

        commands.add("/lmapmanager");
        commands.add("/lmmm");
    }


    @SubscribeEvent
    public void onPlayerChat(ClientChatEvent event) {
        String message = event.getMessage();
        System.out.println("A");
        boolean zash = false;
        for (String command : commands) {
            if (command.toLowerCase().startsWith(message)) {
                zash = true;
            }
        }
        if (!zash) {
            return;
        }
        

        event.setCanceled(true);

        mc.player.sendMessage(new TextComponentString("Сработало..."));

    }

}
