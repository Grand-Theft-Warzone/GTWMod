package com.grandtheftwarzone.gtwmod.api.gui.phone;

public enum PhoneState {
    OPENING,
    OPENED_DISPLAY,
    VERTICAL_TO_HORIZONTAL,
    HORIZONTAL_TO_VERTICAL,
    FULL_SCREEN_TO_HORIZONTAL,
    FULL_SCREEN_TO_VERTICAL,
    HORIZONTAL_TO_FULL_SCREEN,
    VERTICAL_TO_FULL_SCREEN,
    OPENED_APP,
    CLOSING;


    public static PhoneState from(int index){
        return values()[index] == null ? OPENING : values()[index];
    }

    public static PhoneState fromShape(PhoneShape current, PhoneShape newShape){
        if(current == newShape) return OPENED_DISPLAY;
        if(current == PhoneShape.HORIZONTAL && newShape == PhoneShape.VERTICAL) return HORIZONTAL_TO_VERTICAL;
        if(current == PhoneShape.VERTICAL && newShape == PhoneShape.HORIZONTAL) return VERTICAL_TO_HORIZONTAL;
        if(current == PhoneShape.HORIZONTAL && newShape == PhoneShape.FULL_SCREEN) return HORIZONTAL_TO_FULL_SCREEN;
        if(current == PhoneShape.VERTICAL && newShape == PhoneShape.FULL_SCREEN) return VERTICAL_TO_FULL_SCREEN;
        if(current == PhoneShape.FULL_SCREEN && newShape == PhoneShape.HORIZONTAL) return FULL_SCREEN_TO_HORIZONTAL;
        if(current == PhoneShape.FULL_SCREEN && newShape == PhoneShape.VERTICAL) return FULL_SCREEN_TO_VERTICAL;
        return OPENED_DISPLAY;
    }


}
