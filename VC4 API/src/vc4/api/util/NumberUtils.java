/**
 * 
 */
package vc4.api.util;

import java.text.DecimalFormat;

/**
 * @author Paul Durbaba
 *
 */
public class NumberUtils {

	private static DecimalFormat formatter = new DecimalFormat("#,###");
	
	
	/**
	 * Formats a String with digit grouping
	 * 
	 * @param input The number to format
	 * @return The Formatted number as a String
	 */
	public static String doDigitGrouping(int input){
		if(input > -1) return formatter.format(input);
		else return "\u221E";
	}
	/**
	 * Formats a String with digit grouping
	 * 
	 * @param input The number to format
	 * @return The Formatted number as a String
	 */
	public static String doDigitGrouping(long input){
		return formatter.format(input);
	}
	/**
	 * Formats a String with digit grouping
	 * 
	 * @param input The number to format
	 * @return The Formatted number as a String
	 */
	public static String doDigitGrouping(short input){
		return formatter.format(input);
	}
	/**
	 * Formats a String with digit grouping
	 * 
	 * @param input The number to format
	 * @return The Formatted number as a String
	 */
	public static String doDigitGrouping(byte input){
		return formatter.format(input);
	}
	/**
	 * Formats a String with digit grouping
	 * 
	 * @param input The number to format
	 * @return The Formatted number as a String
	 */
	public static String doDigitGrouping(float input){
		return formatter.format(input);
	}
	/**
	 * Formats a String with digit grouping
	 * 
	 * @param input The number to format
	 * @return The Formatted number as a String
	 */
	public static String doDigitGrouping(double input){
		return formatter.format(input);
	}
}
