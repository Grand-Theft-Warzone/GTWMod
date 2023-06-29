package me.phoenixra.gtwclient.utils;

import net.minecraft.client.renderer.Tessellator;

public class Utils {
    private static boolean fmlDetected = false;
    private static boolean seargeNames = false;
    public static String getObfuscatedFieldName(String fieldName, String obfuscatedFieldName, String seargeFieldName) {
        boolean deobfuscated = Tessellator.class.getSimpleName().equals("Tessellator");
        return deobfuscated ? (seargeNames ? obfuscatedFieldName : fieldName) : (fmlDetected ? seargeFieldName : obfuscatedFieldName);
    }
}
