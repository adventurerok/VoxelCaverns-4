/**
 * 
 */
package vc4.api.logging;

import static vc4.api.logging.Level.*;

import java.awt.Color;
import java.util.EnumMap;

import vc4.api.util.Colors;

/**
 * @author paul
 *
 */
public abstract class ColorOutputHandler extends OutputHandler{


	
	protected static EnumMap<Level, String> colors = new EnumMap<Level, String>(Level.class);
	
	static{
		colors.put(INFO, color(Color.white));
		colors.put(SEVERE, color(Color.red));
		colors.put(WARNING, color(Colors.orangeRed));
		colors.put(FATAL, color("c80000"));
		colors.put(DEBUG, color(Colors.lightGreen));
		colors.put(FINE, color(Colors.dodgerBlue));
		colors.put(FINER, color(Colors.deepSkyBlue));
		colors.put(Level.FINEST, color(Colors.indigo));
	}
	
	private static String color(Color c){
		return "{c:" + Integer.toString(c.getRGB() & 0xFFFFFF, 16) + "}";
	}
	
	private static String color(String c){
		return "{c:" + c + "}";
	}
	
	

}
