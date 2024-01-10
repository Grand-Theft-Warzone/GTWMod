package com.grandtheftwarzone.gtwmod.api.player;

import com.grandtheftwarzone.gtwmod.api.GtwAPI;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    @Getter @Setter @Accessors(chain = true)
    private String rank;
    @Getter @Setter @Accessors(chain = true)
    private String gang;

    @Getter @Setter @Accessors(chain = true)
    private int level;
    @Getter @Setter @Accessors(chain = true)
    private double experience;
    @Getter @Setter @Accessors(chain = true)
    private double experienceCap;

    @Getter @Setter @Accessors(chain = true)
    private double money;

    @Getter
    private ConcurrentHashMap<String,String> other;


    public PlayerData(){
        level = 1;
        experience = 0;
        experienceCap = 0;
        money = 0;
        rank = "";
        gang = "";
        other = new ConcurrentHashMap<>();
    }
    public PlayerData(int level, double experience, double experienceCap){
        this.level = level;
        this.experience = experience;
        this.experienceCap = experienceCap;
        money = 0;
        rank = "";
        gang = "";
        other = new ConcurrentHashMap<>();
    }


    public String getOtherOrDefault(String key, String defaultValue){
        return other.getOrDefault(key, defaultValue);
    }
    public PlayerData putOther(String key, String value) {
        this.other.put(key, value);
        return this;
    }


    public static PlayerData asEmptyPacket(){
        return new PlayerData(0,0,0).setMoney(-0.0001).setRank("null").setGang("null");
    }


    public String parseOtherDataToString(){
        if(other.isEmpty()) return "null";
        StringBuilder sb = new StringBuilder();
        for(String key : other.keySet()){
            sb.append(key).append(":").append(other.get(key)).append("_");
        }
        return sb.toString();
    }
    public void insertOtherDataFromString(String data){
        String[] split = data.split("_");
        for (String s : split) {
            String[] otherSplit = s.split(":");
            other.put(otherSplit[0], otherSplit[1]);
        }
    }
    public void fromString(String data){
        try {
            String[] split = data.split("_");
            rank = split[0];
            gang = split[1];
            level = Integer.parseInt(split[2]);
            experience = Double.parseDouble(split[3]);
            experienceCap = Double.parseDouble(split[4]);
            money = Double.parseDouble(split[5]);
            for (int i = 6; i < split.length; i++) {
                String[] otherSplit = split[i].split(":");
                other.put(otherSplit[0], otherSplit[1]);
            }
        }catch (Exception e){
            GtwAPI.getInstance().getGtwMod().getLogger().error("Error while parsing player data: "+data);
        }
    }
    @Override
    public String toString() {
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
}
