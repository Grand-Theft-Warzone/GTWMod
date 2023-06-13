package me.phoenixra.playerhud.utils;


public class MathUtils {

    public static Pair<Integer,Integer> calculateSizeForScreen(int width, int height, int screenWidth, int screenHeight) {
        double ratioX = (double) width / 1920.0;
        double ratioY = (double) height / 1080.0;

        return new Pair<>((int) (ratioX*screenWidth), (int) (ratioY*screenHeight));
    }













    private MathUtils() {
        throw new IllegalStateException("Utility class");
    }



}
