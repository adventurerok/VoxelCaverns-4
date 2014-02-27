/**
 * 
 */
package vc4.api.logging;

/**
 * @author paul
 * 
 */
public class StdOutHandler extends OutputHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.OutputHandler#printText(vc4.api.logging.Level, java.lang.String)
	 */
	@Override
	protected void printText(Level level, String text) {
		if (level.level >= Level.WARNING.level) System.err.println(text);
		else System.out.println(text);
	}

}
