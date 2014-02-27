/**
 * 
 */
package vc4.api.graphics.texture;

import java.io.IOException;

/**
 * @author paul
 * 
 */
public interface AnimatedTextureLoader {

	public AnimatedTexture loadTexture(String path) throws IOException;
}
