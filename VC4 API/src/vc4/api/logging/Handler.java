/**
 * 
 */
package vc4.api.logging;

/**
 * @author paul
 * 
 */
public interface Handler {

	public void handleLog(Logger logger, Level level, Object message);

	public void handleLog(Logger logger, Level level, Object message, Throwable throwable);

	public void close();
}
