package me.phoenixra.libs.atum.json.minidev.json.reader;

import java.io.IOException;

import me.phoenixra.libs.atum.json.minidev.asm.Accessor;
import me.phoenixra.libs.atum.json.minidev.asm.BeansAccess;
import me.phoenixra.libs.atum.json.minidev.json.JSONObject;
import me.phoenixra.libs.atum.json.minidev.json.JSONStyle;
import me.phoenixra.libs.atum.json.minidev.json.JSONUtil;

public class BeansWriterASM implements JsonWriterI<Object> {
	public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
		try {
			Class<?> cls = value.getClass();
			boolean needSep = false;
			@SuppressWarnings("rawtypes")
			BeansAccess fields = BeansAccess.get(cls, JSONUtil.JSON_SMART_FIELD_FILTER);
			out.append('{');
			for (Accessor field : fields.getAccessors()) {
				@SuppressWarnings("unchecked")
				Object v = fields.get(value, field.getIndex());
				if (v == null && compression.ignoreNull())
					continue;
				if (needSep)
					out.append(',');
				else
					needSep = true;
				String key = field.getName();
				JSONObject.writeJSONKV(key, v, out, compression);
			}
			out.append('}');
		} catch (IOException e) {
			throw e;
		}
	}
}
