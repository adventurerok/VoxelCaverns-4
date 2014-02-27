/**
 * 
 */
package vc4.api.client;

/**
 * The launcher for the client
 * 
 * @author paul
 * 
 */
public interface ClientLauncher {

	/**
	 * Creates a lwjgl window
	 * 
	 * @param title
	 *            The title of the window
	 * @param width
	 *            The width of the window
	 * @param height
	 *            The height of the window
	 * @throws GraphicsException
	 *             if the window fails to set its size
	 * @return The newly created ClientWindow
	 */
	public ClientWindow createGameWindow(String title, int width, int height);

	/**
	 * Loads the preferences for the launcher
	 */
	public void loadLauncherPreferences();

	/**
	 * Checks for and installs updates for the game
	 */
	public void checkForUpdates();
}
