/**
 * 
 */
package vc4.api.gui;

import vc4.api.font.RenderedText;

/**
 * @author paul
 * 
 */
public class TextComponent extends Component {

	private Object _text;
	private float fontSize = 14;
	private RenderedText renderedText;

	public void setText(Object text) {
		_text = text;
	}

	public String getText() {
		return _text.toString();
	}

	public void setFontSize(float size) {
		fontSize = size;
	}

	/**
	 * @return the fontSize
	 */
	public float getFontSize() {
		return fontSize;
	}

	public void setRenderedText(RenderedText renderedText) {
		this.renderedText = renderedText;
	}

	public RenderedText getRenderedText() {
		return renderedText;
	}
}
