/**
 * 
 */
package vc4.api.client;

import vc4.api.entity.EntityPlayer;

/**
 * @author paul
 *
 */
public class Client {

	private static ClientGame _game;
	
	/**
	 * @return the game
	 */
	public static ClientGame getGame() {
		return _game;
	}
	
	public static EntityPlayer getPlayer(){
		return _game.getPlayer();
	}
	
	public static ClientServer getServer(){
		return _game.getServer();
	}
	
	/**
	 * @param game the game to set
	 */
	public static void setGame(ClientGame game) {
		_game = game;
	}
	public static boolean debugMode(){
		return true;
	}
}
