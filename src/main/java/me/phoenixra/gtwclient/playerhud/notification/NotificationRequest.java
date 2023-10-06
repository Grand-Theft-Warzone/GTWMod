package me.phoenixra.gtwclient.playerhud.notification;


import lombok.Getter;

public class NotificationRequest {
    @Getter
    private String text;
    @Getter
    private long displayTime;
    @Getter
    private int positionX;
    @Getter
    private int positionY;
    @Getter
    private int fontSize;

    @Getter
    private boolean playSound;
    @Getter
    private boolean started;
    private long finishTime;
    public NotificationRequest(String text,
                               boolean playSound,
                               long displayTime,
                               int positionX,
                               int positionY,
                               int fontSize
    ){
        this.text = text;
        this.displayTime = displayTime;

        this.positionX = positionX;
        this.positionY = positionY;
        this.fontSize = fontSize;

        this.playSound = playSound;
    }

    public void start(){
        started = true;
        finishTime = System.currentTimeMillis() + displayTime;
    }
    public boolean isFinished(){
        if(!started) return false;
        return System.currentTimeMillis() >= finishTime;
    }
}
