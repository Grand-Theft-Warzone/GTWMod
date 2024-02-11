package com.grandtheftwarzone.gtwmod.api.player;


import lombok.Getter;

public class NotificationRequest {
    @Getter
    private String text;
    @Getter
    private long displayTime;
    /**
     * Create a new notification request with the given parameters
     *
     * @param text Text to display
     * @param displayTime How long should the notification be displayed
     */
    public NotificationRequest(String text,
                               long displayTime
    ){
        this.text = text;
        this.displayTime = displayTime;

    }
}
