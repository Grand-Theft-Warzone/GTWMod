package me.phoenixra.libs.atum.json.jsonpath.spi.cache;

import me.phoenixra.libs.atum.json.jsonpath.JsonPath;

public class NOOPCache implements Cache {

    @Override
    public JsonPath get(String key) {
        return null;
    }

    @Override
    public void put(String key, JsonPath value) {
    }
}
