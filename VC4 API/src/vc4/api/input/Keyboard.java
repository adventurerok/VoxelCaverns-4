/**
 * 
 */
package vc4.api.input;

/**
 * @author paul
 * 
 */
public interface Keyboard {

	/**
	 * 
	 * @param key
	 *            The key to check
	 * @return if the key was pressed
	 */
	public boolean keyPressed(Key key);

	/**
	 * 
	 * @param key
	 *            The key to check
	 * @return if the key was released
	 */
	public boolean keyReleased(Key key);

	/**
	 * 
	 * @param key
	 *            The key to check
	 * @return if the key is down
	 */
	public boolean isKeyDown(Key key);

	/**
	 * 
	 * @param key
	 *            The key to check
	 * @return if the key was down before the last two updates
	 */
	public boolean wasKeyDown(Key key);

	/**
	 * Updates the keyboard, moving all stored keys into the old buffer and clearing the current buffer for new key checks
	 */
	public void update();

	/**
	 * Goes to the next key event
	 * 
	 * @return if there is another key event
	 */
	public boolean next();

	public Key getEventKey();

	public char getEventChar();

	/**
	 * 
	 * @return true if the key was pressed, false if released
	 */
	public boolean getEventKeyPressed();

	public void clearEvents();
}
