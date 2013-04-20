/**
 * 
 */
package vc4.client.font;

import vc4.api.font.FontRenderer;
import vc4.api.font.FontRendererFactory;

/**
 * @author paul
 *
 */
public class ClientFontRendererFactory implements FontRendererFactory{

	/* (non-Javadoc)
	 * @see vc4.api.font.FontRendererFactory#createFontRenderer(java.lang.String, float)
	 */
	@Override
	public FontRenderer createFontRenderer(String font, float defaultSize) {
		return new ClientFontRenderer(font, defaultSize);
	}
	
	/**
	 * 
	 */
	public ClientFontRendererFactory() {
		FontRenderer.setFactory(this);
	}


}
