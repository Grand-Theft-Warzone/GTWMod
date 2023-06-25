package me.phoenixra.gtwclient.playerhud.notification;


public class NotificationRequest {
    private String text;

    private long displayTime;

    private float positionX;
    private float positionY;

    private double sizeX;
    private double sizeY;

    private boolean playSound;

    private boolean started;
    private long finishTime;
    public NotificationRequest(String text,
                               boolean playSound,
                               long displayTime,
                               float positionX,
                               float positionY,
                               double sizeX,
                               double sizeY
    ){
        this.text = text;
        this.displayTime = displayTime;

        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.playSound = playSound;
    }


    public long getDisplayTime() {
        return displayTime;
    }

    public String getText() {
        return text;
    }

    public float getPositionX() {
        return positionX;
    }
    public float getPositionY() {
        return positionY;
    }

    public double getSizeX() {
        return sizeX;
    }
    public double getSizeY() {
        return sizeY;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public boolean isStarted() {
        return started;
    }

    public void start(){
        started = true;
        finishTime = System.currentTimeMillis() + displayTime;
    }
    public boolean isFinished(){
        if(!started) return false;
        return System.currentTimeMillis() >= finishTime;
    }

    public boolean isPlaySound() {
        return playSound;
    }
}
