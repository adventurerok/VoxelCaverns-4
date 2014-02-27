/**
 * 
 */
package vc4.api.logging;

import vc4.api.client.ClientGame;
import vc4.api.client.ClientWindow;

/**
 * @author paul
 * 
 */
public class ChatBoxHandler extends ColorOutputHandler {

	private static ClientGame g;

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.OutputHandler#printText(vc4.api.logging.Level, java.lang.String)
	 */
	@Override
	protected void printText(Level level, String text) {
		if (level.level < Level.DEBUG.level) return;
		if (g == null) {
			ClientWindow w = ClientWindow.getClientWindow();
			if (w != null) g = w.getGame();
		}
		if (g != null) {
			g.printChatLine(colors.get(level) + text);
		}
	}

}
