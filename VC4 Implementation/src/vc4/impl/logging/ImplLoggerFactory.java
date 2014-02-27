/**
 * 
 */
package vc4.impl.logging;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import vc4.api.logging.*;

/**
 * @author paul
 * 
 */
public class ImplLoggerFactory implements LoggerFactory {

	private ConcurrentHashMap<Object, Logger> map = new ConcurrentHashMap<Object, Logger>();

	protected Vector<Handler> handlers = new Vector<Handler>();

	/**
	 * 
	 */
	public ImplLoggerFactory() {
		handlers.add(new StdOutHandler());
		Logger.setFactory(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.LoggerFactory#getLogger(java.lang.String)
	 */
	@Override
	public Logger getLogger(String name) {
		return logger(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.LoggerFactory#getLogger(java.lang.Class)
	 */
	@Override
	public Logger getLogger(Class<?> clazz) {
		return logger(clazz);
	}

	private Logger logger(Object o) {
		if (map.containsKey(o)) return map.get(o);

		ImplLogger log = new ImplLogger();
		log.name = (o instanceof Class) ? ((Class<?>) o).getName() : o.toString();

		for (int d = 0; d < handlers.size(); ++d) {
			log.addHandler(handlers.get(d));
		}

		map.put(o, log);
		if (o instanceof Class) map.put(((Class<?>) o).getName(), log);

		return log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.LoggerFactory#getDefaultHandlers()
	 */
	@Override
	public List<Handler> getDefaultHandlers() {
		return handlers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.LoggerFactory#addDefaultHandler(vc4.api.logging.Handler)
	 */
	@Override
	public void addDefaultHandler(Handler handler) {
		handlers.add(handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.logging.LoggerFactory#removeDefaultHandler(vc4.api.logging.Handler)
	 */
	@Override
	public void removeDefaultHandler(Handler handler) {
		handlers.remove(handler);
	}

}
