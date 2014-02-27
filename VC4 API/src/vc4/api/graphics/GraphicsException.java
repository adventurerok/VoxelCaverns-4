/**
 * 
 */
package vc4.api.graphics;

/**
 * @author paul
 * 
 *         Caused by a LWJGLException or OpenGLException in the client package
 * 
 */
public class GraphicsException extends RuntimeException {

	public GraphicsException(String message, Throwable cause) {
		super(message, cause);

	}

	public GraphicsException(Throwable cause) {
		super(cause);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5893564466350429607L;

}
