package me.phoenixra.gtwclient.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.util.Objects;
import java.util.function.Consumer;

public class Config {
    @Getter
    private String id;
    private JsonElement jsonElement;
    private File file;

    public Config(@NotNull File file) {
        if(!file.getName().matches("\\w+\\.json")) throw new IllegalArgumentException("File have to be a .json file");
        this.id = file.getName().split("\\.json")[0];
        this.file = file;
        JsonParser jsonParser = new JsonParser();
        try(JsonReader reader = new JsonReader(new FileReader(file))) {

            this.jsonElement = Objects.requireNonNull(jsonParser.parse(reader));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void loadConfig(Consumer<JsonElement> consumer){
        consumer.accept(this.jsonElement);
    }


}
