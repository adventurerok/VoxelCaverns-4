/**
 * 
 */
package vc4.impl.logging;

import java.util.*;

import vc4.api.logging.*;

/**
 * @author paul
 *
 */
public class ImplLogger extends Logger {

	protected String name;
	
	Vector<Handler> handlers = new Vector<Handler>();
	
	/* (non-Javadoc)
	 * @see vc4.api.logging.Logger#log(vc4.api.logging.Level, java.lang.Object)
	 */
	@Override
	public void log(Level level, Object message) {
		for(int d = 0; d< handlers.size(); ++d){
			handlers.get(d).handleLog(this, level, message);
		}
	}

	/* (non-Javadoc)
	 * @see vc4.api.logging.Logger#log(vc4.api.logging.Level, java.lang.Object, java.lang.Throwable)
	 */
	@Override
	public void log(Level level, Object message, Throwable throwable) {
		for(int d = 0; d< handlers.size(); ++d){
			handlers.get(d).handleLog(this, level, message, throwable);
		}
	}

	/* (non-Javadoc)
	 * @see vc4.api.logging.Logger#addHandler(vc4.api.logging.Handler)
	 */
	@Override
	public void addHandler(Handler handler) {
		handlers.add(handler);
	}

	/* (non-Javadoc)
	 * @see vc4.api.logging.Logger#removeHandler(vc4.api.logging.Handler)
	 */
	@Override
	public void removeHandler(Handler handler) {
		handlers.remove(handler);
	}

	/* (non-Javadoc)
	 * @see vc4.api.logging.Logger#getHandlers()
	 */
	@Override
	public List<Handler> getHandlers() {
		return handlers;
	}

	/* (non-Javadoc)
	 * @see vc4.api.logging.Logger#getName()
	 */
	@Override
	public String getName() {
		return name;
	}


}
