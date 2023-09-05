package me.phoenixra.gtwclient.api.gui;

import lombok.Getter;
import me.phoenixra.atumodcore.api.tuples.Pair;
import me.phoenixra.gtwclient.api.gui.impl.GuiElementButton;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GtwGuiMenu extends GuiScreen {
    @Getter
    private HashMap<GuiElementLayer, List<GuiElement>> elements = new HashMap<>();

    @Getter
    private int savedScaleFactor;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        long time = System.currentTimeMillis();
        savedScaleFactor = RenderUtils.getScaleFactor();
        float windowRatioX = (float) (savedScaleFactor / RenderUtils.getWindowRatioWidth() );
        float windowRatioY = (float) (savedScaleFactor / RenderUtils.getWindowRatioHeight());

        for (int layer = 0; layer < GuiElementLayer.values().length; layer++) {
            List<GuiElement> list = elements.get(GuiElementLayer.getLayer(layer));
            if (list == null) {
                continue;
            }
            for (GuiElement element : list) {
                element.draw(savedScaleFactor, windowRatioX, windowRatioY, mouseX, mouseY);
            }
        }
        System.out.println("Draw time: " + (System.currentTimeMillis() - time));
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
