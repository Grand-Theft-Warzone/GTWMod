package com.grandtheftwarzone.gtwmod.core.map.globalmap.element;

import com.grandtheftwarzone.gtwmod.api.utils.GLUtils;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas.CanvasGlobalmap;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.data.DataDrawTextMarker;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementNameMarker extends BaseElement {

    @Setter
    private DataDrawTextMarker drawTextMarker;

    public ElementNameMarker(@NotNull AtumMod atumMod, @NotNull int layer, @Nullable DisplayCanvas elementOwner, DataDrawTextMarker drawTextMarker) {
        super(atumMod, 50, 0, 0, 0, 0, elementOwner);
        this.drawTextMarker = drawTextMarker;
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {
//        float proporziaSize = (float) drawTextMarker.getSizeText() / 12;
//        float textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(drawTextMarker.getText()) * proporziaSize;
//        float textHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * proporziaSize;
//        System.out.println("TW: " + textWidth + " <> " + textHeight);
//            RenderUtils.drawRect(drawTextMarker.getPosX(), drawTextMarker.getPosY(), (int) textWidth, (int) textHeight, drawTextMarker.getColor());

        boolean go = true;
        if (getElementOwner() instanceof CanvasGlobalmap) {
            go = ((CanvasGlobalmap) getElementOwner()).getSubCanvas() == null;
        }
        if (drawTextMarker != null && go) {
            GLUtils.drawText(drawTextMarker.getPosX(), drawTextMarker.getPosY(), drawTextMarker.getText(), drawTextMarker.getColor(), drawTextMarker.getSizeText());
        }
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
