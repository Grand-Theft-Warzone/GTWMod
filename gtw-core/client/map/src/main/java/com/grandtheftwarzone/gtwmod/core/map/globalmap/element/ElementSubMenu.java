package com.grandtheftwarzone.gtwmod.core.map.globalmap.element;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import com.grandtheftwarzone.gtwmod.api.GtwLog;
import com.grandtheftwarzone.gtwmod.api.utils.GLUtils;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.canvas.CanvasMapSubmenu;
import com.grandtheftwarzone.gtwmod.core.map.globalmap.data.DataMapSubMenu;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementSubMenu extends BaseElement {

    @Getter
    private final DataMapSubMenu data;

    @Setter
    private boolean hovered = false;

    private final float[] brightnessOnHover = new float[]{0.65F,0.65F,0.65F};

    public ElementSubMenu(@NotNull AtumMod atumMod, int drawPriority, int x, int y, int width, int height, @Nullable DisplayCanvas elementOwner, DataMapSubMenu data) {
        super(atumMod, drawPriority, x, y, width, height, elementOwner);
        this.data = data;
    }

    @Override
    protected void onDraw(DisplayResolution displayResolution, float v, int i, int i1) {
//
//        if ((getLastMouseX() >= getX() && getLastMouseX() <= getX() + getWidth()) && getLastMouseY() >= getY() && getLastMouseY() <= getY() + getHeight()) {
//            hovered = true;
//            System.out.println("Наведён.");
//        }

        int posTextX = getX() + data.getSizeIcon() + data.getIndentIcons() + data.getEdgeMargin();
        int posTextY = getY() + ((CanvasMapSubmenu) getElementOwner()).getIndentText()/2;

        int posIconX = getX() + data.getEdgeMargin();
        int posIconY = getY() + ((CanvasMapSubmenu) getElementOwner()).getIndentText()/2;

        RenderUtils.bindTexture(data.getIcon());
        RenderUtils.drawCompleteImage(posIconX, posIconY, data.getSizeIcon(), data.getSizeIcon());
        GLUtils.drawText(posTextX, posTextY, data.getName(), data.getColorName(), data.getSizeText(), false);


    }

    public void onClick() {
        String[] actions = data.getActionStr().split("@");
        DisplayAction action = GtwAPI.getInstance().getGtwMod().getDisplayManager().getActionRegistry().getActionById(actions[0]);

        if (action == null) {
            GtwLog.getLogger().error("[ERROR] Action with id " + actions[0] + " not found!");
            return;
        }

        action.perform(
                ActionData.builder()
                        .atumMod(GtwAPI.getInstance().getGtwMod())
                        .attachedElement(this)
                        .attachedEvent(null)
                        .mouseX(0)
                        .mouseY(0)
                        .actionArgs(new ActionArgs(actions[1]))
                        .build()
        );
        getElementOwner().onRemove();
    }

    @Override
    protected BaseElement onClone(BaseElement baseElement) {
        return baseElement;
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {

    }
}
