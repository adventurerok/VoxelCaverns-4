/**
 * 
 */
package vc4.api.gui;

/**
 * @author paul
 * 
 */
public class TextComponent extends Component {

	private Object _text;
	private float fontSize = 14;

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

}
