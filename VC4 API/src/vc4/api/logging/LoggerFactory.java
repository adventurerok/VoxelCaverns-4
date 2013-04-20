/**
 * 
 */
package vc4.api.logging;

import java.util.List;

/**
 * @author paul
 *
 */
public interface LoggerFactory {

	public Logger getLogger(String name);
	public Logger getLogger(Class<?> clazz);
	
	public List<Handler> getDefaultHandlers();
	public void addDefaultHandler(Handler handler);
	public void removeDefaultHandler(Handler handler);
}
