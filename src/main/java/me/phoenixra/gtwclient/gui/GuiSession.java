package me.phoenixra.gtwclient.gui;

import java.util.HashMap;

public class GuiSession {
    private final HashMap<String, String> data = new HashMap<>();

    public void setData(String id, String value){
        data.put(id,value);
    }
    public String getData(String id){
        return data.get(id);
    }
}
