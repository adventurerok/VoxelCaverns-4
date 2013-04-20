/**
 * 
 */
package vc4.api.client;

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
