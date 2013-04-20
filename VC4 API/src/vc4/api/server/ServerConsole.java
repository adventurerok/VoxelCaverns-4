/**
 * 
 */
package vc4.api.server;

/**
 * @author paul
 *
 */
public abstract class ServerConsole {

	private static ServerConsole console;
	
	/**
	 * @param console the console to set
	 */
	public static void setConsole(ServerConsole console) {
		ServerConsole.console = console;
	}
	
	/**
	 * @return the console
	 */
	public static ServerConsole getConsole() {
		return console;
	}

	/**
	 * @param line
	 */
	public abstract void write(String line);

	/**
	 * @param line
	 */
	public void writeLine(String line) {
	}

	

}
