package com.grandtheftwarzone.gtwmod.core.map.action;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.misc.AtumColor;

import java.io.IOException;

@RegisterDisplayAction(templateId = "zoom_minimap")
public class ActionZoom implements DisplayAction {

    private double coef = 2;
    private int setZoom;
    private int zoom;

    private String defaultZoom = "250";

    private final String id = "zoom_minimap";


    @Override
    public void perform(ActionData actionData) {

        DisplayRenderer renderer = actionData.getAttachedElement().getElementOwner().getDisplayRenderer();



        if (renderer == null) return;
        if(actionData.getAttachedElement()==null) return;

        zoom = Integer.parseInt(renderer.getDisplayData().getDataOrDefault(id, defaultZoom));
        String[] args = actionData.getActionArgs().getArgs();

        if (args[0].equalsIgnoreCase("update_default")) {
            Config config = (Config) renderer.getBaseCanvas().getSettingsConfig();
            renderer.getBaseCanvas().getSettingsConfig().getSubsection("default_data").set(id, String.valueOf(zoom));
            try {
                assert config != null;
                ((LoadableConfig)config).save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            GtwLog.getLogger().debug("Saving the minimap zoom value (" + zoom + ")");
            return;
        }


        if (args[0].equalsIgnoreCase("set_default")) {
            if (args.length == 1) {
                Config config = (Config) renderer.getBaseCanvas().getSettingsConfig();
                renderer.getBaseCanvas().getSettingsConfig().getSubsection("default_data").set(id, defaultZoom);try {
                    assert config != null;
                    ((LoadableConfig)config).save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GtwLog.getLogger().debug("[SET] Saving the minimap zoom value (" + zoom + ")");
                return;
            } else if (args.length == 2) {
                Config config = (Config) renderer.getBaseCanvas().getSettingsConfig();
                renderer.getBaseCanvas().getSettingsConfig().getSubsection("default_data").set(id, String.valueOf(args[1]));
                try {
                    assert config != null;
                    ((LoadableConfig)config).save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GtwLog.getLogger().debug("[SET] Saving the minimap zoom value (" + zoom + ")");
                return;
            }
        }

        assert args != null;

        if (args.length == 2) {
            coef = Double.parseDouble(args[1]);
        }
        int addZoom = (int)(zoom + coef);
        int removeZoom = (int)(zoom - coef);

        int maxZoom = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMaxZoom();
        int minZoom = GtwAPI.getInstance().getMapManagerClient().getMinimapManager().getMinZoom();
        if (((addZoom >= maxZoom || addZoom <= minZoom) && args[0].equalsIgnoreCase("add")) || ((removeZoom >= maxZoom || removeZoom <= minZoom) && args[0].equalsIgnoreCase("remove"))) {
            GtwAPI.getInstance().getMapManagerClient().getMinimapManager().setColorFrame(AtumColor.RED, 9);

            DisplayAction action = GtwAPI.getInstance().getGtwMod().getDisplayManager().getActionRegistry().getActionById("play_sound");
            if(action==null) {
                return;
            }
            action.perform(
                    ActionData.builder()
                            .atumMod(GtwAPI.getInstance().getGtwMod())
                            .attachedElement(null)
                            .attachedEvent(null)
                            .mouseX(0)
                            .mouseY(0)
                            .actionArgs(new ActionArgs("minecraft:block.note.harp;1;0.5"))
                            .build()
            );

            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            renderer.getDisplayData().setData(id, String.valueOf(addZoom));
            return;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            renderer.getDisplayData().setData(id, String.valueOf(removeZoom));
            return;
        }

        if (args[0].equalsIgnoreCase("update_zoom")) {
            setZoom = Integer.parseInt(args[1]);
            renderer.getDisplayData().setData(id, String.valueOf(setZoom));
            return;
        }

    }
}
