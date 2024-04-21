package com.grandtheftwarzone.gtwmod.api.gui.phone.canvas;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneState;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CanvasPhone extends BaseCanvas {
    public static final float horizontalShapeScale = 1.5f;

    @Getter
    protected PhoneState state = PhoneState.OPENING;
    @Getter
    protected PhoneShape shape = PhoneShape.VERTICAL;


    @Getter
    private List<PhoneApp> apps;

    @Getter
    protected PhoneApp openedApp;

    @Getter @Setter
    private boolean locked = true;



    public CanvasPhone(@NotNull AtumMod atumMod, DisplayCanvas owner) {
        super(atumMod, owner);
        apps = GtwAPI.getInstance()
                .getPhoneManager().getAppDrawingOrder();
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution,
                          float scaleFactor, int mouseX, int mouseY) {
        //if display data has 'close' record and it's true
        // -> call backPressed for an app, or close the phone
        if (getDisplayRenderer().getDisplayData().
                getDataOrDefault("close", "false")
                .equalsIgnoreCase("true")
                && state != PhoneState.CLOSING) {

            if (state == PhoneState.OPENED_APP) {
                boolean closeApp = getOpenedApp().onPressedBack(this);
                getDisplayRenderer().getDisplayData().setData("close", "false");

                if (closeApp) closeApp();

            } else {
                setState(PhoneState.CLOSING);
            }
        }

    }

    @Override
    public void updateBaseVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateBaseVariables(config, configKey);
        apps = GtwAPI.getInstance()
                .getPhoneManager().getAppDrawingOrder();
        for(PhoneApp app : apps){
            app.updateVariables(this,
                    config.getSubsection("apps." + app.getId())
            );
        }
    }

    public void openApp(PhoneApp app){
        if(state == PhoneState.OPENING || state == PhoneState.CLOSING) return;
        if(openedApp!=null){
            openedApp.onAppClose(this);
        }
        System.out.println("Opening app: " + app.getAppName());
        openedApp = app;
        app.onAppOpen(this);
        if (app.getShapeRequired() != PhoneShape.VERTICAL) {
            changeShape(app.getShapeRequired());
            return;
        }
        setState(PhoneState.OPENED_APP);
    }
    public void closeApp() {
        if (getOpenedApp() == null) return;

        if (getOpenedApp().getShapeRequired() != PhoneShape.VERTICAL) {
            changeShape(PhoneShape.VERTICAL);

        } else {
            setState(PhoneState.OPENED_DISPLAY);
        }
        openedApp.onAppClose(this);
        openedApp = null;

    }

    public abstract int getDisplayX();
    public abstract int getDisplayY();

    public abstract int getDisplayWidth();
    public abstract int getDisplayHeight();

    @Override
    public int getGlobalX() {
        return super.getGlobalX() + (getDisplayX() - super.getGlobalX());
    }

    @Override
    public int getGlobalY() {
        return super.getGlobalY() + (getDisplayY() - super.getGlobalY());
    }

    @Override
    public void onRemove() {
        super.onRemove();
        apps.forEach(app -> app.onAppClose(this));
    }

    protected abstract void setState(@NotNull PhoneState state);

    public abstract void changeShape(@NotNull PhoneShape shape);


    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        CanvasPhone canvasPhone = (CanvasPhone) baseElement;
        canvasPhone.shape = PhoneShape.VERTICAL;
        canvasPhone.state = PhoneState.OPENING;
        return canvasPhone;
    }
}
