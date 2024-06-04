package com.grandtheftwarzone.gtwmod.core.map.globalmap;

import com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas.CanvasGlobalmap;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Objects;

public class GtwGlobalmapScreen extends BaseScreen {

    public GtwGlobalmapScreen(@NotNull AtumMod atumMod, @NotNull String id) {
        super(atumMod, Objects.requireNonNull(atumMod.getDisplayManager().getElementRegistry().getDrawableCanvas(id)));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();


        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            GtwGlobalmapScreen globalmapScreen = (GtwGlobalmapScreen) Minecraft.getMinecraft().currentScreen;
            CanvasGlobalmap canvasGlobalmap = (CanvasGlobalmap) globalmapScreen.getRenderer().getBaseCanvas();
            canvasGlobalmap.onScrollEvent(wheel);
        }
    }

}