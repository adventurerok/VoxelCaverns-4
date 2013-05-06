/**
 * 
 */
package vc4.api.client;

import vc4.api.GameState;
import vc4.api.entity.EntityPlayer;
import vc4.api.gui.Component;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.MouseSet;
import vc4.api.util.Setting;

/**
 * @author paul
 *
 */
public interface ClientGame {

	public void action(String action);
	public void draw();
	public void update();
	public void load();
	public void unload();
	public void resized();
	public boolean isPaused();
	public void setPaused(boolean paused);
	
	/**
	 * Finds the window
	 * @return The game window
	 */
	public ClientWindow getWindow();
	
	public void addComponent(Component c);
	public MouseSet getMouseSet();
	public ColorScheme getColorScheme(String name);
	public String[] getColorSchemeNames();
	public String[] getFontNames();
	public Setting<Object> getSetting(String name);
	public void changeSetting(String name, Object setting);
	
	/**
	 * @return The current color scheme
	 */
	public Setting<Object> getCurrentColorScheme();
	public Setting<Object> getCurrentCrosshair();
	
	/**
	 * Finds the unique id of the currently displaying menu
	 * @return The unique id of the currently displaying menu
	 */
	public long getMenuState();
	
	public void printChatLine(String text);
	public void clearChatBox();
	public LoadingScreen getLoadingScreen();
	public GameState getGameState();
	public void setGameState(GameState g);
	public int getCrosshair(String name);
	public EntityPlayer getBlockInteractor();
}
