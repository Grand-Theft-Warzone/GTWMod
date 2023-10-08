package com.grandtheftwarzone.gtwclient.core.phone.apps.notifications;

import com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwclient.api.gui.phone.PhoneGui;
import org.jetbrains.annotations.NotNull;

public class NotificationsApp implements PhoneApp {
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
        return "Notifications";
    }

    @Override
    public @NotNull String getId() {
        return "notifications";
    }
}
