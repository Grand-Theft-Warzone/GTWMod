package com.grandtheftwarzone.gtwmod.core.display;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.screen.ScreensManager;
import com.grandtheftwarzone.gtwmod.core.display.actions.ActionPlaySound;
import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GtwScreensManager implements ScreensManager, AtumModService {
    @Getter
    private final String id = "screensManager";
    private CustomMainMenu customMainMenu;

    public GtwScreensManager(){
        GtwAPI.getInstance().getGtwMod().provideModService(this);
    }
    @Override
    public void handleFmlEvent(@NotNull FMLEvent fmlEvent) {
       // empty
    }

    @Override
    public void onRemove() {

    }
    @Override
    public @NotNull GuiScreen getMainMenuGuiScreen() {
        if(customMainMenu != null){
            customMainMenu.getRenderer().closeRenderer();
        }

        customMainMenu = new CustomMainMenu();
        return customMainMenu;
    }


    private static class CustomMainMenu extends BaseScreen {

        public CustomMainMenu() {
            super(GtwAPI.getInstance().getGtwMod(),
                    Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().getDisplayManager()
                            .getElementRegistry().getDrawableCanvas(
                            Objects.requireNonNull(GtwAPI.getInstance().getGtwMod().
                                            getConfigManager().getConfig("settings"))
                                    .getString("main_menu")
                    )));
        }
    }
}
