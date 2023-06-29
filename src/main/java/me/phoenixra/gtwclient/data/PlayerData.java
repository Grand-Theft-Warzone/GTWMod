package me.phoenixra.gtwclient.data;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.gtwclient.gui.api.BaseGUI;
import me.phoenixra.gtwclient.playerhud.notification.NotificationRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    @Getter
    private String rank;
    @Getter
    private String gang;

    @Getter
    private int level;
    @Getter
    private double experience;
    @Getter
    private double experienceCap;

    @Getter
    private double money;

    @Getter
    private ConcurrentHashMap<String,String> other;


    @Getter
    private BaseGUI openedGui;

    @Getter
    private final List<NotificationRequest> notificationQueue;

    public PlayerData(){
        level = 1;
        experience = 0;
        experienceCap = 0;
        money = 0;
        rank = "";
        gang = "";
        notificationQueue = Collections.synchronizedList(new ArrayList<>());
        other = new ConcurrentHashMap<>();
    }
    public PlayerData(int level, double experience, double experienceCap){
        this.level = level;
        this.experience = experience;
        this.experienceCap = experienceCap;
        money = 0;
        rank = "";
        notificationQueue = new ArrayList<>();
    }

    public NotificationRequest getQueuedNotification(){
        return notificationQueue.size()>0 ? notificationQueue.get(0) : null;
    }
    public NotificationRequest nextQueuedNotification(){
        if(notificationQueue.size()<1) return null;
        notificationQueue.remove(0);
        return getQueuedNotification();
    }

    public String getOtherOrDefault(String key, String defaultValue){
        return other.getOrDefault(key, defaultValue);
    }

    public PlayerData setRank(String rank) {
        this.rank = rank;
        return this;
    }
    public PlayerData setGang(String gang) {
        this.gang = gang;
        return this;
    }
    public PlayerData setLevel(int level) {
        this.level = level;
        return this;
    }

    public PlayerData setExperience(double experience) {
        this.experience = experience;
        return this;
    }

    public PlayerData setExperienceCap(double experienceCap) {
        this.experienceCap = experienceCap;
        return this;
    }

    public PlayerData setMoney(double money) {
        this.money = money;
        return this;
    }
    public PlayerData putOther(String key, String value) {
        this.other.put(key, value);
        return this;
    }
    public void setOpenedGui(BaseGUI openedGui) {
        this.openedGui = openedGui;
    }


    public String toPlainText(){
        StringBuilder sb = new StringBuilder();
        for(String key : other.keySet()){
            sb.append(key).append(":").append(other.get(key)).append("_");
        }
        return  rank+"_"+
                gang+"_"+
                level+"_"+
                experience+"_"+
                experienceCap+"_"+
                money+"_"+sb.toString();

    }

    public void fromString(String data){
        String[] split = data.split("_");
        rank = split[0];
        gang = split[1];
        level = Integer.parseInt(split[2]);
        experience = Double.parseDouble(split[3]);
        experienceCap = Double.parseDouble(split[4]);
        money = Double.parseDouble(split[5]);
        for(int i = 6; i<split.length; i++){
            String[] otherSplit = split[i].split(":");
            other.put(otherSplit[0], otherSplit[1]);
        }
    }
}
