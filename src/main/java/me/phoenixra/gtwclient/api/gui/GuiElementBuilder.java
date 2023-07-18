package me.phoenixra.gtwclient.api.gui;

import me.phoenixra.gtwclient.api.gui.functions.PositionFunction;
import me.phoenixra.gtwclient.api.gui.impl.GuiElementButton;

import java.util.function.Function;

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
    GuiElementBuilder setX(PositionFunction functionX);
    GuiElementBuilder setY(PositionFunction functionY);
    GuiElementBuilder setWidth(PositionFunction functionWidth);
    GuiElementBuilder setHeight(PositionFunction functionHeight);

    GuiElementBuilder setLayer(GuiElementLayer layer);
    GuiElement build();
}
