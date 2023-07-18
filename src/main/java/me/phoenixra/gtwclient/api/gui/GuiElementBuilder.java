package me.phoenixra.gtwclient.api.gui;

import com.google.common.collect.Lists;
import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.utils.Pair;


public interface GuiElementBuilder {
    default GuiElementBuilder setX(int value){
        return setX((f)-> value);
    }
    default GuiElementBuilder setY(int value){
        return setY((f)-> value);
    }
    default GuiElementBuilder setWidth(int value){
        return setWidth((f)-> value);
    }
    default GuiElementBuilder setHeight(int value){
        return setHeight((f)-> value);
    }
    default GuiElementBuilder setX(String jsonPath){
        return setX((scaleFactor)->
                (int)(GTWClient.settings.getStringEvaluated(jsonPath,
                        Lists.newArrayList(
                                new Pair<>(
                                        "%scale_factor%",
                                        ()->String.valueOf(getGuiMenu().getSavedScaleFactor())
                                ),
                                new Pair<>(
                                        "%screen_height%",
                                        ()->String.valueOf(getGuiMenu().height)
                                ),
                                new Pair<>(
                                        "%screen_width%",
                                        ()->String.valueOf(getGuiMenu().width)
                                )
                        )
                ))
        );
    }
    default GuiElementBuilder setY(String jsonPath){
        return setX((f)->
                (int)(GTWClient.settings.getStringEvaluated(jsonPath,
                        Lists.newArrayList(
                                new Pair<>(
                                        "%scale_factor%",
                                        ()->String.valueOf(getGuiMenu().getSavedScaleFactor())
                                ),
                                new Pair<>(
                                        "%screen_height%",
                                        ()->String.valueOf(getGuiMenu().height)
                                ),
                                new Pair<>(
                                        "%screen_width%",
                                        ()->String.valueOf(getGuiMenu().width)
                                )
                        )
                ))
        );
    }
    default GuiElementBuilder setWidth(String jsonPath){
        return setX((f)->
                (int)(GTWClient.settings.getStringEvaluated(jsonPath,
                        Lists.newArrayList(
                                new Pair<>(
                                        "%scale_factor%",
                                        ()->String.valueOf(getGuiMenu().getSavedScaleFactor())
                                ),
                                new Pair<>(
                                        "%screen_height%",
                                        ()->String.valueOf(getGuiMenu().height)
                                ),
                                new Pair<>(
                                        "%screen_width%",
                                        ()->String.valueOf(getGuiMenu().width)
                                )
                        )
                ))
        );
    }
    default GuiElementBuilder setHeight(String jsonPath){
        return setX((f)->
                (int)(GTWClient.settings.getStringEvaluated(jsonPath,
                        Lists.newArrayList(
                                new Pair<>(
                                        "%scale_factor%",
                                        ()->String.valueOf(getGuiMenu().getSavedScaleFactor())
                                ),
                                new Pair<>(
                                        "%screen_height%",
                                        ()->String.valueOf(getGuiMenu().height)
                                ),
                                new Pair<>(
                                        "%screen_width%",
                                        ()->String.valueOf(getGuiMenu().width)
                                )
                        )
                ))
        );
    }
    GuiElementBuilder setX(PositionFunction functionX);
    GuiElementBuilder setY(PositionFunction functionY);
    GuiElementBuilder setWidth(PositionFunction functionWidth);
    GuiElementBuilder setHeight(PositionFunction functionHeight);

    default GuiElementBuilder setLayer(String jsonPath){
        return setLayer(GuiElementLayer.fromStringOrDefault(
                GTWClient.settings.getStringValue(jsonPath),
                GuiElementLayer.MIDDLE
        ));
    }
    default GuiElementBuilder setLayer(String jsonPath, GuiElementLayer defaultLayer){
        return setLayer(GuiElementLayer.fromStringOrDefault(
                GTWClient.settings.getStringValue(jsonPath),
                defaultLayer
        ));
    }
    GuiElementBuilder setLayer(GuiElementLayer layer);

    GtwGuiMenu getGuiMenu();
    GuiElement build();
}
