/**
 * 
 */
package vc4.api.graphics.texture;

import java.io.IOException;

/**
 * @author paul
 *
 */
public interface SheetTextureLoader {

	public SheetTexture loadTexture(String path) throws IOException;

}
