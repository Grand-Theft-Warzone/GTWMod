package com.grandtheftwarzone.gtwmod.core.phone.apps.notifications;

import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.annotations.RegisterPhoneApp;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.NotNull;

@RegisterPhoneApp
public class NotificationsApp implements PhoneApp {


    @Override
    public void draw(BaseCanvas parent) {

    }

    @Override
    public void drawIcon(@NotNull BaseCanvas parent, int x, int y, int width, int height) {

    }

    @Override
    public void onOpen(PhoneManager parent) {

    }

    @Override
    public void onClosed(PhoneManager parent) {

    }

    @Override
    public @NotNull PhoneShape getShapeRequired() {
        return null;
    }

    @Override
    public int getAppPriority() {
        return 0;
    }

    @Override
    public @NotNull String getAppName() {
        return null;
    }

    @Override
    public @NotNull String getId() {
        return null;
    }
}
