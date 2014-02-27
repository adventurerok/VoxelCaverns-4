/**
 * 
 */
package vc4.api.input;

/**
 * @author paul
 * 
 */
public class Input {

	private static Keyboard _keys;
	private static Mouse _ms;

	public static void setKeyboard(Keyboard keys) {
		_keys = keys;
	}

	public static void setMouse(Mouse ms) {
		_ms = ms;
	}

	/**
	 * @return the Client Keyboard, or null if server
	 */
	public static Keyboard getClientKeyboard() {
		return _keys;
	}

	/**
	 * @return the Client Mouse, or null if server
	 */
	public static Mouse getClientMouse() {
		return _ms;
	}

}
