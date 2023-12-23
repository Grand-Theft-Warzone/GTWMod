package com.grandtheftwarzone.gtwmod.core.display.hud.elements;

import com.grandtheftwarzone.gtwmod.api.player.PlayerData;
import com.grandtheftwarzone.gtwmod.core.display.GtwPlayerHUD;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElement;
import com.grandtheftwarzone.gtwmod.core.display.hud.HudElementType;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import net.minecraft.client.gui.Gui;

public class HudElementGangPrefix extends HudElement {
    public HudElementGangPrefix() {
        super(HudElementType.GANG_PREFIX, 0, 0, 0, 0);
        parent = HudElementType.WIDGET;
    }

    @Override
    public void drawElement(Gui gui, int scaledWidth, int scaledHeight) {
        float posXLocal;
        float posYLocal = 46;
        PlayerData pd = GtwPlayerHUD.instance.playerData;
        String rank = pd.getGang();
        //reduce the x pos by the length of the string, but get rid of color codes before

        posXLocal = scaledWidth - mc.fontRenderer.getStringWidth(
                StringUtils.removeColorCodes(rank)
        ) - 2;
        mc.fontRenderer.drawStringWithShadow(rank.replace("&", "\u00a7"),
                posXLocal,
                posYLocal,
                0xAEED7A
        );

    }
}
