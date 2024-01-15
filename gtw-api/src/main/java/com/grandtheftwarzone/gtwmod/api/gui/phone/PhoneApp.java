package com.grandtheftwarzone.gtwmod.api.gui.phone;

import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.NotNull;

public interface PhoneApp {

    /**
     * Draw the app on the phone
     * @param parent the phone gui
     */
    void draw(BaseCanvas parent);

    /**
     * Draw the app icon
     * @param x the x position
     * @param y the y position
     * @param width the icon width
     * @param height the icon height
     * @param parent the phone gui
     */
    void drawIcon(@NotNull BaseCanvas parent, int x, int y, int width, int height);


    /**
     * Called when the client pressed
     * the app button
     * <p>Use it to load data</p>
     *
     * @param parent the phone gui
     */
    void onOpen(PhoneManager parent);

    /**
     * Called when the app is closed
     * <p>Use it to clear cache</p>
     *
     * @param parent the phone gui
     */
    void onClosed(PhoneManager parent);


    /**
     * Get the phone shape required by the app
     * @return the phone shape required
     */
    @NotNull
    PhoneShape getShapeRequired();

    /**
     * Get the app priority
     * It affects on the app drawing order on the phone
     * @return the app priority
     */
    int getAppPriority();
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
