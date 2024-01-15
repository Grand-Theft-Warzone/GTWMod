package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneManager;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.core.display.elements.ElementImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CanvasPhone extends BaseCanvas {


    private PhoneShape currentShape;
    private ElementImage background;

    private List<PhoneApp> appDrawingOrder;

    private ElementImage appScreen;

    private int animationTimer;

    private boolean init;
    private boolean closing;

    public CanvasPhone(AtumMod atumMod) {
        super(atumMod, null);
        background = new ElementImage(
                atumMod,
                this
        );
        appScreen = new ElementImage(
                atumMod,
                this
        );
        currentShape = PhoneShape.VERTICAL_CENTRALIZED;
        appDrawingOrder = GtwAPI.getInstance()
                .getPhoneGui().getAppDrawingOrder();

    }

    @Override
    protected void onDraw(float scaleFactor,
                          float scaleX, float scaleY,
                          int mouseX, int mouseY) {
        background.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        appScreen.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);

        for(int i = 0; i < appDrawingOrder.size(); i++){
            PhoneApp app = appDrawingOrder.get(i);
            int iconSize = 32;
            int iconSpace = 10;
            int totalSpace = iconSize + iconSpace;
            int totalSize = totalSpace * appDrawingOrder.size();
            int startX = getOriginX() + (getOriginWidth() / 2) - (totalSize / 2);
            int startY = getOriginY() + (getOriginHeight() / 2) - (iconSize / 2);
            int iconX = startX + (totalSpace * i);
            int iconY = startY;
            app.drawIcon(this, iconX, iconY, iconSize, iconSize);
        }


    }

    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateVariables(config, configKey);
        if(config.hasPath("background")){
            Config config1 = config.getSubsection("background");
            background.updateVariables(config1, "background");
            background.setOriginX(getOriginX());
            background.setOriginY(getOriginY());
            background.setOriginWidth(getOriginWidth());
            background.setOriginHeight(getOriginHeight());
        }
        if(config.hasPath("appScreen")){
            Config config1 = config.getSubsection("appScreen");
            appScreen.updateVariables(config1, "appScreen");
            appScreen.setOriginX(getOriginX()+appScreen.getOriginX());
            appScreen.setOriginY(getOriginY()+appScreen.getOriginY());
            appScreen.setOriginWidth(getOriginWidth()+appScreen.getOriginWidth());
            appScreen.setOriginHeight(getOriginHeight()+appScreen.getOriginHeight());
        }
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas baseCanvas) {
        return baseCanvas;
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }






    private static class PhoneDrawHelper{

    }
}
