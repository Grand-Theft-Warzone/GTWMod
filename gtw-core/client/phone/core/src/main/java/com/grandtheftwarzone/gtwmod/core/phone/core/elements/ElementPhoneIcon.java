package com.grandtheftwarzone.gtwmod.core.phone.core.elements;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;


@RegisterDisplayElement(templateId = "phone_app_icon")
public class ElementPhoneIcon extends BaseElement {

    private String appId;
    private int size;
    private PhoneApp appCached;

    public ElementPhoneIcon(@NotNull AtumMod atumMod,
                            @Nullable DisplayCanvas elementOwner
    ) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution,
                          float v, int mouseX, int mouseY) {
        if(appCached==null){
            appCached = GtwAPI.getInstance().getPhoneManager()
                    .getApp(appId);
            if(appCached==null) {
                throw new RuntimeException(
                        "Phone app with id "+appId+" nit found!"
                );
            }
        }

        appCached.drawIcon(
                displayResolution,
                getX(),
                getY(),
                getWidth(),
                isCoordinateInElement(mouseX,mouseY)
        );
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        ((ElementPhoneIcon)baseElement).appCached = null;
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
        appId = config.getString("appId");
        size = config.getInt("size");
        getOriginWidth().setDefaultValue(size);
        getOriginHeight().setDefaultValue(size);

    }

    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseInputEvent.Pre e) {
        int pressed = Mouse.getEventButton();
        if (Mouse.getEventButtonState() && pressed == 0) {
            if(!isCoordinateInElement(getLastMouseX(),getLastMouseY())){
                return;
            }
            GtwAPI.getInstance().getPhoneManager()
                    .tryOpenApp(appId);
        }
    }
}
