/**
 * 
 */
package vc4.api.text;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

import vc4.api.Resources;
import vc4.api.logging.Logger;

/**
 * @author paul
 *
 */
public class Localization {

	private static HashMap<String, String> loaded = new HashMap<String, String>();
	
	public static void loadLocalization(String name){
		loaded.clear();
		for(URL url : Resources.getResourceURLs()){
			String build = url.toString();
			if(!build.endsWith("/")) build = build + "/";
			build = build + "lang/" + name + ".lang";
			try{
				URL n = new URL(build);
				loadLocalization(n.openStream());
			} catch(IOException e){
				
			}
		}
	}
	
	public static void loadLocalization(InputStream input) throws IOException{
		if(input == null){
			Logger.getLogger(Localization.class).warning("Input is null");
			return;
		}
		BufferedReader read = new BufferedReader(new InputStreamReader(input));
		loadLines(read);
	}
	
	public static void loadLines(BufferedReader r) throws IOException{
		String currentLine;
		while((currentLine = r.readLine()) != null){
			currentLine = currentLine.trim();
			if(currentLine.equals("")) continue;
			if(currentLine.startsWith("#")) continue;
			String[] parts = currentLine.split("=");
			try{
				addLocalization(parts[0].toLowerCase(), parts[1]);
			}
			catch(Exception exc){
				exc.printStackTrace();
			}
		}
	}
	
	public static void addLocalization(String name, String value){
		loaded.put(name, value.replace("\\n", "\n").replace("\\t", "\t"));
	}
	
	public static String getLocalization(String text){
		String s = loaded.get(text);
		//return s != null ? s : text;
		return s;
	}
	
	public static String getLocalization(String text, Object... args){
		String format = loaded.get(text);
		if(format == null) return text;
		for (int dofor = 0; dofor < args.length; dofor++) {
			String string = args[dofor].toString();
			String replace = "{" + dofor + "}";
			format = format.replace(replace, (string == null) ? "" : string);
		}
		return format;
	}
	
	public static String getLocalization(String text, String... args){
		String format = loaded.get(text);
		if(format == null) return text;
		for (int dofor = 0; dofor < args.length; dofor++) {
			String string = args[dofor];
			String replace = "{" + dofor + "}";
			format = format.replace(replace, (string == null) ? "" : string);
		}
		return format;
	}

}
