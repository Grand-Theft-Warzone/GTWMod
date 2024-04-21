package com.grandtheftwarzone.gtwmod.api.gui.phone;

import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PhoneManager {


    /**
     * Add an app to the phone
     * @param app the app to add
     */
    void addApp(@NotNull PhoneApp app);

    /**
     * Remove an app from the phone
     * @param app the app to remove
     */
    void removeApp(@NotNull PhoneApp app);

    void tryOpenApp(String appId);
    /**
     * Get an app by its id
     * @param id the app id
     * @return the app or null if not found
     */
    @Nullable
    PhoneApp getApp(@NotNull String id);

    @NotNull List<PhoneApp> getAppDrawingOrder();

}
