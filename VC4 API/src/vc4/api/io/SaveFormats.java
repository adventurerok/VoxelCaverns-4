package vc4.api.io;

import java.util.HashMap;

public class SaveFormats {

	private static HashMap<String, SaveFormat> formats = new HashMap<>();

	public static void registerSaveFormat(String name, SaveFormat format) {
		formats.put(name.toUpperCase(), format);
	}

	public static SaveFormat getSaveFormat(String name) {
		return formats.get(name.toUpperCase());
	}
}
