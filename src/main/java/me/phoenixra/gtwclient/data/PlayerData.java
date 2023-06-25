package me.phoenixra.gtwclient.data;

import me.phoenixra.gtwclient.gui.api.BaseGUI;
import me.phoenixra.gtwclient.playerhud.notification.NotificationRequest;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    private int level;
    private double experience;
    private double experienceCap;

    private double money;

    private String rank;



    private BaseGUI openedGui;

    private final List<NotificationRequest> notificationQueue;

    public PlayerData(){
        level = 1;
        experience = 0;
        experienceCap = 0;
        money = 0;
        rank = "";
        notificationQueue = new ArrayList<>();
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

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public double getExperienceCap() {
        return experienceCap;
    }

    public double getMoney() {
        return money;
    }

    public String getRank() {
        return rank;
    }

    public BaseGUI getOpenedGui() {
        return openedGui;
    }


    public List<NotificationRequest> getNotificationQueue() {
        return notificationQueue;
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

    public PlayerData setRank(String rank) {
        this.rank = rank;
        return this;
    }
    public void setOpenedGui(BaseGUI openedGui) {
        this.openedGui = openedGui;
    }
}
