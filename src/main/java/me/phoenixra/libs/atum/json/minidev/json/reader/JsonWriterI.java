package me.phoenixra.libs.atum.json.minidev.json.reader;

import java.io.IOException;

import me.phoenixra.libs.atum.json.minidev.json.JSONStyle;

public interface JsonWriterI<T> {
	public <E extends T> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException;
}
