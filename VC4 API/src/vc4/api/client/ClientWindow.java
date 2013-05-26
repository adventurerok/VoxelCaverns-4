package vc4.api.client;

import vc4.api.graphics.RenderType;
import vc4.api.vector.Vector2i;

/**
 * The Window created for the game to run in
 * 
 * @author paul
 *
 */
public abstract class ClientWindow {

	private static ClientWindow _clientWindow;
	public static final int MIN_WIDTH = 800;
	public static final int MIN_HEIGHT = 600;
	
	/**
	 * Finds the game window in singleplayer
	 * @return The client window, or null if it is a server
	 */
	public static ClientWindow getClientWindow() {
		return _clientWindow;
	}
	
	protected ClientWindow() {
		_clientWindow = this;
		setWidth(MIN_WIDTH);
		setHeight(MIN_HEIGHT);
	}
	
	/**
	 * Runs the window. Do not use
	 */
	public abstract void run();
	protected abstract void draw();
	protected abstract void update();
	protected abstract void load();
	protected abstract void resized();
	protected abstract void unload();
	
	/**
	 * Sets the title of the window
	 * @param title the new title of the window
	 * @return the old title, for convenience
	 */
	public abstract String setTitle(String title);
	
	/**
	 * Finds title of the window
	 * @return The title of the singleplayer window
	 */
	public abstract String getTitle();
	
	/**
	 * Sets the width of the window
	 * @param width The new width of the window
	 * @throws GraphicsException if width fails to be set
	 * @return The old width of the window, for convenience
	 */
	public abstract int setWidth(int width);
	
	/**
	 * Finds the width of the window
	 * @return The width of the singleplayer window
	 */
	public abstract int getWidth();
	
	/**
	 * Sets the height of the window
	 * @param height The new height of the window
	 * @throws GraphicsException if height fails to be set
	 * @return The old height of the window, for convenience
	 */
	public abstract int setHeight(int height);
	
	/**
	 * Finds the height of the window
	 * @return The height of the singleplayer window
	 */
	public abstract int getHeight();
	
	/**
	 * Closes the window
	 */
	public abstract void close();
	
	/**
	 * Finds if the user requested a close
	 * @return If the window is due to close
	 */
	public abstract boolean isCloseRequested();
	
	/**
	 * Finds if the LWJGL Display is created
	 * @return if The LWJGL is created
	 */
	public abstract boolean isCreated();
	
	public abstract void enterRenderMode(RenderType mode);
	
	
	/**
	 * Finds the game using the window
	 * @return The client game or null if server
	 */
	public abstract ClientGame getGame();

	public Vector2i getDimensions(){
		return new Vector2i(getWidth(), getHeight());
	}
	

}
