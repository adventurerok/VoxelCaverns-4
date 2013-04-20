/**
 * 
 */
package vc4.api.graphics.texture;

import java.io.IOException;
import java.net.URL;

/**
 * @author paul
 *
 */
public interface TextureLoader {

	public Texture loadTexture(String pathInJar) throws IOException;

	/**
	 * @param url
	 * @return A loaded texture
	 * @throws IOException
	 */
	public Texture loadTexture(URL url) throws IOException;
}
