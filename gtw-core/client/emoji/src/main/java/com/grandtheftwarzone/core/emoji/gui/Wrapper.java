package com.grandtheftwarzone.core.emoji.gui;

public class Wrapper<T> {

    private T value;

    public Wrapper(T initialValue) {
        this.value = initialValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
