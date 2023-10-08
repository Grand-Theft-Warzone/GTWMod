package com.grandtheftwarzone.gtwclient.api.gui;

import lombok.Getter;

public enum GuiAction {
    FACTORY_CLOSE(-1),
    FACTORY_OPEN(0),
    FACTORY_COLLECT(0),
    FACTORY_CLAIM(1),
    FACTORY_UPGRADE_PRODUCTION(2),
    FACTORY_UPGRADE_STORAGE(3),
    FACTORY_UPDATE_DATA(10);

    @Getter
    private int id;
    GuiAction(int id) {
        this.id = id;
    }
}
