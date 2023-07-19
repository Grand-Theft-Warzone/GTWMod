package me.phoenixra.gtwclient.api.gui;

public interface GuiElement {

    void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY);

    default boolean isHovered(int mouseX, int mouseY){
        GtwGuiMenu menu = getGuiMenu();
        if(getLayer().getLayerNumber()+1 < GuiElementLayer.values().length) {
            for (int layer = getLayer().getLayerNumber() + 1; layer < GuiElementLayer.values().length; layer++) {
                if (menu.getElementInPosition(GuiElementLayer.getLayer(layer), mouseX, mouseY) != null) {
                    return false;
                }
            }
        }
        return mouseX >= getX() &&
                mouseX < getX() + getWidth()
                && mouseY >= getY()
                && mouseY < getY() + getHeight();
    }
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    GuiElementLayer getLayer();

    default void register(){
        getGuiMenu().addElement(getLayer(),this);
    }

    GtwGuiMenu getGuiMenu();
}
