package com.grandtheftwarzone.gtwclient.core.phone.apps.property;

import com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneGui;
import com.grandtheftwarzone.gtwclient.api.gui.phone.annotations.RegisterPhoneApp;
import org.jetbrains.annotations.NotNull;

@RegisterPhoneApp
public class PropertyApp implements PhoneApp {
    @Override
    public void draw(PhoneGui parent) {

    }

    @Override
    public void onOpen(PhoneGui parent) {

    }

    @Override
    public void onClosed(PhoneGui parent) {

    }

    @Override
    public @NotNull String getAppName() {
        return "Property";
    }

    @Override
    public @NotNull String getId() {
        return "property";
    }
}
