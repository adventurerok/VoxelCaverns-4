/**
 * 
 */
package vc4.api.font;

import vc4.api.vector.Vector2f;

/**
 * @author paul
 * 
 */
public abstract class FontRenderer {

	private static FontRendererFactory factory;

	/**
	 * @param factory
	 *            the factory to set
	 */
	public static void setFactory(FontRendererFactory factory) {
		FontRenderer.factory = factory;
	}

	public static FontRenderer createFontRenderer(String font, float defaultSize) {
		return factory.createFontRenderer(font, defaultSize);
	}

	public abstract Vector2f measureString(String text, float size);

	public abstract RenderedText renderString(float x, float y, String text);

	public abstract RenderedText renderString(float x, float y, String text, float size);

	public abstract void resetStyles();

	public abstract void setAlpha(float alpha);

}
