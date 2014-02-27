/**
 * 
 */
package vc4.api.util;

import java.util.Arrays;

/**
 * @author Paul Durbaba
 * 
 */
public class BooleanUtils {

	public static String formatBoolean(String t, String f, boolean b) {
		if (b) return t;
		else return f;
	}

	/**
	 * Formats a boolean into common words. Supported Words: YES, NO, TRUE, FALSE, ON, OFF ACCEPT, DENY, ACCEPTED, DENIED If you were to pass in "yes" and true, it would return "yes" If you were to pass in "accept" and false, it would
	 * return "deny"
	 * 
	 * @param guess
	 *            The word to format the boolean into, can be any of the supported words
	 * @param b
	 *            The boolean to format
	 * @return The formated boolean
	 * @throws IllegalArgumentException
	 *             if the guess is not supported
	 * @see #formatBoolean(String, String, boolean)
	 */
	public static String formatBoolean(String guess, boolean b) {
		if (guess.equalsIgnoreCase("YES") || guess.equalsIgnoreCase("NO")) return formatBoolean("yes", "no", b);
		else if (guess.equalsIgnoreCase("ON") || guess.equalsIgnoreCase("OFF")) return formatBoolean("on", "off", b);
		else if (guess.equalsIgnoreCase("TRUE") || guess.equalsIgnoreCase("FALSE")) return formatBoolean("true", "false", b);
		else if (guess.equalsIgnoreCase("ACCEPTED") || guess.equalsIgnoreCase("DENIED")) return formatBoolean("accepted", "denied", b);
		else if (guess.equalsIgnoreCase("ACCEPT") || guess.equalsIgnoreCase("DENY")) return formatBoolean("accept", "deny", b);
		else throw new IllegalArgumentException("This does not yet support " + guess);

	}

	public static boolean areAllFalse(boolean[] in) {
		for (int dofor = 0; dofor < in.length; ++dofor) {
			if (in[dofor]) return false;
		}
		return true;
	}

	public static boolean areAllTrue(boolean[] in) {
		for (int dofor = 0; dofor < in.length; ++dofor) {
			if (!in[dofor]) return false;
		}
		return true;
	}

	public static boolean[] createTrueArray(int size) {
		boolean b[] = new boolean[size];
		Arrays.fill(b, true);
		return b;
	}
}
