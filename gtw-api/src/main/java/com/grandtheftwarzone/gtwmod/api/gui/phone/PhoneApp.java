package com.grandtheftwarzone.gtwmod.api.gui.phone;

import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import me.phoenixra.atumconfig.api.config.Config;

import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;

public interface PhoneApp {

    /**
     * Draw an app on the phone
     * IMPORTANT: the app is drawn on the phone display,
     * so, use the displayX, displayY, displayWidth and displayHeight
     * to have an app properly displayed within edges
     *
     * @param parent the phone gui
     * @param resolution the phone resolution
     * @param scaleFactor the scale factor
     * @param displayWidth the width of the phone display
     * @param displayHeight the height of the phone display
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     */
    void draw(@NotNull CanvasPhone parent,
              @NotNull DisplayResolution resolution,
              float scaleFactor,
              int displayWidth, int displayHeight,
              int mouseX, int mouseY);

    /**
     * Draw the app icon
     * @param x the x position
     * @param y the y position
     * @param size the icon size
     */
    void drawIcon(@NotNull DisplayResolution resolution,
                  int x, int y, int size,
                  boolean isHovered);


    /**
     * Called when the canvas phone updates its data
     *
     * @param canvasPhone the phone gui
     * @param config config section of the app
     */
    void updateVariables(@NotNull CanvasPhone canvasPhone,
                         @NotNull Config config);

    /**
     * Called when the app is opened but not yet displayed
     * <p>Use it to load cache</p>
     *
     * @param parent the phone gui
     */
    default void onAppOpen(@NotNull CanvasPhone parent){

    }

    /**
     * Called when the app is closed
     * <p>Use it to clear cache</p>
     *
     * @param parent the phone gui
     */
    default void onAppClose(@NotNull CanvasPhone parent){

    }


    /**
     * Called when the app is closed
     * <p>Use it to clear cache</p>
     *
     * @param parent the phone gui
     * @return true if the app can be closed
     */
    default boolean onPressedBack(CanvasPhone parent){
        return true;
    }


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
