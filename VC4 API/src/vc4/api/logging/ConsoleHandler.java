/**
 * 
 */
package vc4.api.logging;

import vc4.api.server.ServerConsole;

/**
 * @author paul
 *
 */
public class ConsoleHandler extends ColorOutputHandler{

	private static ServerConsole console;
	
	/**
	 * @param console the console to set
	 */
	public static void setConsole(ServerConsole console) {
		ConsoleHandler.console = console;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.logging.OutputHandler#printText(vc4.api.logging.Level, java.lang.String)
	 */
	@Override
	protected void printText(Level level, String text) {
		if(console != null) console.writeLine(colors.get(level) + text);
	}

	

}
