package me.phoenixra.gtwclient.api.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class Fonts {
    private static HashMap<String, GlyphPage> fonts = new HashMap<>();

    @Nullable
    public static GlyphPage registerFont(@NotNull String id, @NotNull InputStream fontFile){
        if(fonts.containsKey(id)) return fonts.get(id);
        try (InputStream stream = fontFile) {
            Font font = Font.createFont(
                    0,
                    stream
            );
            if(!GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)) return null;
            GlyphPage glyphPage = new GlyphPage(
                    new Font(font.getFontName(), Font.PLAIN, 80),
                    false, false
            );
            char[] chars = new char[256];
            for (int i = 0; i < 256; i++) {
                chars[i] = (char) i;
            }
            glyphPage.generateGlyphPage(chars);
            fonts.put(id, glyphPage);
            return glyphPage;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Nullable
    public GlyphPage getFont(@NotNull String id){
        return fonts.get(id);
    }
}
