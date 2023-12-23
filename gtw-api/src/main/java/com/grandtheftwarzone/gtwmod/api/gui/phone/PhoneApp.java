package com.grandtheftwarzone.gtwmod.api.gui.phone;

import org.jetbrains.annotations.NotNull;

public interface PhoneApp {

    /**
     * Draw the app on the phone
     * @param parent the phone gui
     */
    void draw(PhoneGui parent);


    /**
     * Called when the client pressed
     * the app button
     * <p>Use it to load data</p>
     *
     * @param parent the phone gui
     */
    void onOpen(PhoneGui parent);

    /**
     * Called when the app is closed
     * <p>Use it to clear cache</p>
     *
     * @param parent the phone gui
     */
    void onClosed(PhoneGui parent);


    /**
     * Get the app name which will be
     * displayed below the app button
     * @return the app name
     */
    @NotNull
    String getAppName();

    /**
     * Get the app id
     * @return the app id
     */
    @NotNull
    String getId();
}
