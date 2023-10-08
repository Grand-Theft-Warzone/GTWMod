package com.grandtheftwarzone.gtwclient.api.gui;

import lombok.Getter;

public enum GuiAction {
    PHONE_CLOSE(-1),
    PHONE_OPEN(0),
    PHONE_APP_OPEN(1),
    PHONE_APP_CLOSE(2),
    PHONE_APP_PERFORMED_ACTION(3),


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
