package com.grandtheftwarzone.gtwclient.api.player;


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
    /**
     * Create a new notification request with the given parameters
     * For the positions setup it for the 1920x1080 resolution,
     * the positions will be auto-scaled to the current resolution
     *
     * @param text Text to display
     * @param playSound Should a sound be played when the notification is displayed
     * @param displayTime How long should the notification be displayed
     * @param positionX X position of the notification.
     * @param positionY Y position of the notification
     * @param fontSize Font size of the notification
     */
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


    /**
     * Start the notification
     */
    public void start(){
        started = true;
        finishTime = System.currentTimeMillis() + displayTime;
    }
    /**
     * Check if the notification has
     * been displayed for the given time
     *
     * @return true/false
     */
    public boolean isFinished(){
        if(!started) return false;
        return System.currentTimeMillis() >= finishTime;
    }
}
