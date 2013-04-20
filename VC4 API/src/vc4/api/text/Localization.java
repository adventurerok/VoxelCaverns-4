/**
 * 
 */
package vc4.api.text;

import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import vc4.api.logging.Logger;
import vc4.api.yaml.ThreadYaml;

/**
 * @author paul
 *
 */
public class Localization {

	private static HashMap<String, String> loaded = new HashMap<String, String>();
	
	public static void loadLocalization(String name){
		String path = "vc4/impl/lang/" + name + ".yml";
		Logger.getLogger("VC4").info("Loading language: " + name);
		loadLocalization(Localization.class.getClassLoader().getResourceAsStream(path));
	}
	
	@SuppressWarnings("unchecked")
	public static void loadLocalization(InputStream input){
		if(input == null){
			Logger.getLogger(Localization.class).warning("Input is null");
			return;
		}
		Yaml yaml = ThreadYaml.getYamlForThread();
		Object o = yaml.load(input);
		LinkedHashMap<String, ?> map = (LinkedHashMap<String, ?>) o;
		loadLocalizations("", map);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadLocalizations(String prefix, LinkedHashMap<String, ?> map){
		for(Entry<String, ?> e : map.entrySet()){
			String key = prefix + e.getKey();
			Object v = e.getValue();
			if(v instanceof LinkedHashMap){
				loadLocalizations(key + ".", (LinkedHashMap<String, ?>) v);
			} else{
				if(e.getKey().equals("pval")) key = prefix.substring(0, prefix.length() - 1);
				String value = v.toString();
				loaded.put(key, value);
			}
		}
	}
	
	public static String getLocalization(String text){
		String s = loaded.get(text);
		//return s != null ? s : text;
		return s;
	}
	
	public static String getLocalization(String text, Object[] args){
		String format = loaded.get(text);
		if(format == null) return text;
		for (int dofor = 0; dofor < args.length; dofor++) {
			String string = args[dofor].toString();
			String replace = "{" + dofor + "}";
			format = format.replace(replace, (string == null) ? "" : string);
		}
		return format;
	}

}
