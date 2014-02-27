/**
 * 
 */
package vc4.api.font;

/**
 * @author paul
 * 
 */
public interface FontRendererFactory {

	public FontRenderer createFontRenderer(String font, float defaultSize);
}
