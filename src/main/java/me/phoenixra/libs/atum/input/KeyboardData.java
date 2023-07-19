package me.phoenixra.libs.atum.input;

public class KeyboardData {
    public final int keycode;
    public final char typedChar;

    public KeyboardData(int keycode, char typedChar) {
        this.keycode = keycode;
        this.typedChar = typedChar;
    }
}
