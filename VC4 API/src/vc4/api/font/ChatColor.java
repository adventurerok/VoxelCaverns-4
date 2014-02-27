/**
 * 
 */
package vc4.api.font;

import java.awt.Color;

/**
 * @author paul
 * 
 */
public class ChatColor {

	static Color colors[] = new Color[32];

	static {
		setupColors(0x00000000, // Black
				0x000000be, // Navy
				0x0000be00, // Green
				0x0000bebe, // Cyan
				0x00be0000, // Red
				0x00be00be, // Purple
				0x00D9A334, // Gold
				0x00bebebe, // LightGray
				0x003f3f3f, // Gray
				0x003f3ffe, // Blue
				0x003ffe3f, // LightGreen
				0x003ffefe, // LightBlue
				0x00fe3f3f, // Rose
				0x00fe3ffe, // Light Purple
				0x00fefe3f, // Yellow
				0x00ffffff, // White
				0x008b4513 // Brown
		);
		colors[17] = null;
		colors[18] = new Color(1F, 1F, 1F, 0F);
	}

	public static Color getColor(int symbol) {
		try {
			return colors[symbol];
		} catch (Exception e) {
			throw new RuntimeException("NUM: " + symbol, e);
		}
	}

	private static void setupColors(int... is) {
		for (int dofor = 0; dofor < is.length; dofor++) {
			colors[dofor] = new Color(is[dofor]);
		}
	}

}
