package me.phoenixra.gtwclient.playerhud.elements;

import me.phoenixra.gtwclient.data.PlayerData;
import me.phoenixra.gtwclient.playerhud.Hud;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.playerhud.HudElementType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HudElementGangPrefix extends HudElement{
    public HudElementGangPrefix() {
        super(HudElementType.GANG_PREFIX, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        float posXLocal;
        float posYLocal = 46;
        GlStateManager.disableBlend();
        PlayerData pd = Hud.instance.playerData;
        String rank = "&aBOSS";
        //reduce the x pos by the length of the string, but get rid of color codes before

        posXLocal = scaledWidth - mc.fontRenderer.getStringWidth(removeColorCodes(rank)) - 2;
        mc.fontRenderer.drawStringWithShadow(rank.replace("&", "\u00a7"),
                posXLocal,
                posYLocal,
                0xAEED7A
        );

        GlStateManager.enableBlend();
    }
    public static String removeColorCodes(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '\u00A7' || currentChar == '&') {
                i++;
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }
}
