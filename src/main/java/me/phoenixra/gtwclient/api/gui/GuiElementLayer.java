package me.phoenixra.gtwclient.api.gui;

public enum GuiElementLayer {
    BACKGROUND(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    FOREGROUND(4);
    private final int layer;
    GuiElementLayer(int layer){
        this.layer = layer;
    }
    public int getLayerNumber(){
        return layer;
    }

    public static GuiElementLayer getLayer(int layer){
        for(GuiElementLayer guiElementLayer : GuiElementLayer.values()){
            if(guiElementLayer.getLayerNumber() == layer){
                return guiElementLayer;
            }
        }
        return null;
    }
}
