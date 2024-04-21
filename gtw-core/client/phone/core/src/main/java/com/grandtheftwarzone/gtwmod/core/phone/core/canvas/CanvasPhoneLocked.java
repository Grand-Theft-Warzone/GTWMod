package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;


import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneState;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import me.phoenixra.atumconfig.api.config.Config;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

public class CanvasPhoneLocked extends BaseCanvas {

    protected CanvasPhone phone;

    private boolean unlock;

    private float swiping = 0f;
    private boolean swipingActive = false;

    private int startingPoint;
    private int swipingEndpoint = 100;

    public CanvasPhoneLocked(@NotNull AtumMod atumMod,
                             @NotNull CanvasPhone phone
    ) {
        super(atumMod, phone);
        this.phone = phone;
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {

    }

    @Override
    public void updateBaseVariables(@NotNull Config config, @Nullable String configKey) {

        super.updateBaseVariables(config, configKey);
    }


    @Override
    protected BaseElement onClone(BaseElement baseElement) {

        return baseElement;
    }

    @Override
    public void setDisplayRenderer(@NotNull DisplayRenderer displayRenderer) {

        super.setDisplayRenderer(displayRenderer);
        displayRenderer.getDisplayData().setDefaultData(
                "locked_swiping",
                "0"
        );
        displayRenderer.getDisplayData().setData(
                "locked_swiping",
                String.valueOf(swiping)
        );
    }

    @SubscribeEvent
    public void onMouseSwiping(GuiScreenEvent.MouseInputEvent.Pre e) {
        PhoneState phoneState = phone.getState();
        if (phoneState != PhoneState.OPENED_DISPLAY || !phone.isLocked()) {
            return;
        }
        if (Mouse.isButtonDown(0)
                && Mouse.getEventButtonState()
                && !swipingActive) {
            int edgeY = (phone.getDisplayY() + phone.getDisplayHeight()) - phone.getDisplayHeight() / 4;
            if (getLastMouseY() > edgeY) {
                swipingActive = true;
                startingPoint = getLastMouseY();
                return;
            }
        }
        if (swipingActive) {
            if (Mouse.getEventButton() == 0
                    && !Mouse.getEventButtonState()) {
                if (swiping >= 1) {
                    phone.setLocked(false);
                    swipingActive = false;
                } else {
                    setSwiping(0);
                    swipingActive = false;
                }
                return;
            }
            setSwiping((float) (startingPoint - getLastMouseY())
                    / swipingEndpoint);
        }
    }

    private void setSwiping(float newValue){
        if (newValue < 0) newValue = 0;
        swiping = newValue;
        getDisplayRenderer().getDisplayData().setData(
                "locked_swiping",
                String.valueOf(swiping)
        );
    }

}
