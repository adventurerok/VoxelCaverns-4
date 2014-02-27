/**
 * 
 */
package vc4.api.logging;

import java.util.List;

/**
 * @author paul
 * 
 */
public abstract class Logger {

	private static LoggerFactory factory;

	/**
	 * @param factory
	 *            the factory to set
	 */
	public static void setFactory(LoggerFactory factory) {
		Logger.factory = factory;
	}

	public static Logger getLogger(String name) {
		return factory.getLogger(name);
	}

	public static Logger getLogger(Class<?> clazz) {
		return factory.getLogger(clazz);
	}

	public static List<Handler> getDefaultHandlers() {
		return factory.getDefaultHandlers();
	}

	public static void addDefaultHandler(Handler handler) {
		factory.addDefaultHandler(handler);
	}

	public static void removeDefaultHandler(Handler handler) {
		factory.removeDefaultHandler(handler);
	}

	public abstract void log(Level level, Object message);

	public abstract void log(Level level, Object message, Throwable throwable);

	public void info(Object message) {
		log(Level.INFO, message);
	}

	public void info(Object message, Throwable throwable) {
		log(Level.INFO, message, throwable);
	}

	public void fatal(Object message) {
		log(Level.FATAL, message);
	}

	public void fatal(Object message, Throwable throwable) {
		log(Level.FATAL, message, throwable);
	}

	public void severe(Object message) {
		log(Level.SEVERE, message);
	}

	public void severe(Object message, Throwable throwable) {
		log(Level.SEVERE, message, throwable);
	}

	public void warning(Object message) {
		log(Level.WARNING, message);
	}

	public void warning(Object message, Throwable throwable) {
		log(Level.WARNING, message, throwable);
	}

	public void debug(Object message) {
		log(Level.DEBUG, message);
	}

	public void debug(Object message, Throwable throwable) {
		log(Level.DEBUG, message, throwable);
	}

	public void fine(Object message) {
		log(Level.FINE, message);
	}

	public void fine(Object message, Throwable throwable) {
		log(Level.FINE, message, throwable);
	}

	public abstract void addHandler(Handler handler);

	public abstract void removeHandler(Handler handler);

	public abstract List<Handler> getHandlers();

	public abstract String getName();

}
