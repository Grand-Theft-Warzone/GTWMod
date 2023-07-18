package me.phoenixra.gtwclient.api.gui;

import me.phoenixra.gtwclient.api.gui.impl.GuiElementButton;
import me.phoenixra.gtwclient.utils.Pair;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GtwGuiMenu extends GuiMainMenu {

    private HashMap<GuiElementLayer, List<GuiElement>> elements = new HashMap<>();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int scaleFactor = RenderUtils.getScaleFactor();
        float windowRationWidth = (float) RenderUtils.getWindowRatioWidth();
        float windowRationY = (float) RenderUtils.getWindowRatioHeight();

        for (int layer = 0; layer < GuiElementLayer.values().length; layer++) {
            List<GuiElement> list = elements.get(GuiElementLayer.getLayer(layer));
            if (list == null) {
                continue;
            }
            for (GuiElement element : list) {
                element.draw(scaleFactor, windowRationWidth, windowRationY);
            }
        }
        for(GuiButton button : buttonList){
            button.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected final void actionPerformed(GuiButton button) throws IOException {
       //empty
    }

    public void addElement(GuiElementLayer layer, GuiElement element) {
        if (elements.containsKey(layer)) {
            elements.get(layer).add(element);
        } else {
            List<GuiElement> list = new ArrayList<>();
            list.add(element);
            elements.put(layer, list);
        }
        if (element instanceof GuiElementButton) {
            buttonList.add(((GuiElementButton) element));
        }
    }

    public Pair<GuiElementLayer,GuiElement> getElementInPosition(int x, int y){
        for (int layer = GuiElementLayer.values().length - 1; layer > 0; layer--) {
            GuiElementLayer elementLayer = GuiElementLayer.getLayer(layer);
            List<GuiElement> list = elements.get(elementLayer);
            if (list == null) {
                continue;
            }
            for (GuiElement element : list) {
                if(x >= element.getX() &&
                        y >= element.getY() &&
                        x < element.getX() + element.getWidth() &&
                        y < element.getY() + element.getHeight()){
                    return new Pair<>(elementLayer,element);
                }
            }
        }
        return null;
    }
    public GuiElement getElementInPosition(GuiElementLayer layer, int x, int y){
        List<GuiElement> list = elements.get(layer);
        if (list == null) {
            return null;
        }
        for (GuiElement element : list) {
            if(x >= element.getX() &&
                    y >= element.getY() &&
                    x < element.getX() + element.getWidth() &&
                    y < element.getY() + element.getHeight()){
                return element;
            }
        }
        return null;
    }
}
