/**
 * 
 */
package vc4.client.gui;

import java.awt.Rectangle;
import java.util.ArrayList;

import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.*;

/**
 * @author paul
 * 
 */
public class ChatBox extends Component {

	private FontRenderer font;
	private OpenGL gl;
	private ArrayList<ChatLine> lines = new ArrayList<ChatLine>();

	private boolean open = true;

	/**
	 * 
	 */
	public ChatBox() {
		font = FontRenderer.createFontRenderer("unispaced_14", 14F);
		gl = Graphics.getOpenGL();
		setDefaultBounds(new Rectangle(0, 0, 400, 154));
		setResizer(new ResizerBorder(Border.SOUTHWEST));
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.Component#draw()
	 */
	@Override
	public void draw() {
		super.draw();
		int startLine = lines.size() - 10;
		int endLine = lines.size();
		float x = getX();
		float y = getY();
		float width = getWidth();
		Graphics.getClientShaderManager().bindShader("fontback");
		gl.begin(GLPrimitive.QUADS);
		for (int d = startLine; d < endLine; ++d) {
			if (d < 0) {
				y += 14F;
				continue;
			}
			ChatLine l = lines.get(d);
			if (open || l.getAlpha() > 0.001F) {
				gl.color(0.3F, 0.3F, 0.3F, open ? 1 : l.getAlpha());
				gl.vertex(x, y);
				gl.vertex(x + width, y);
				gl.vertex(x + width, y + 14F);
				gl.vertex(x, y + 14F);
			}
			y += 14F;
		}
		gl.end();
		Graphics.getClientShaderManager().unbindShader();
		y = getY();
		for (int d = startLine; d < endLine; ++d) {
			if (d < 0) {
				y += 14F;
				continue;
			}
			ChatLine l = lines.get(d);
			if (open || l.getAlpha() > 0.001F) {
				if (l.lastRender != null && l.lastRender.getY() == y && l.lastAlpha == l.getAlpha()) {
					l.lastRender.draw();
				} else	{
					font.setAlpha(open ? 1 : l.getAlpha());
					l.lastAlpha = l.getAlpha();
					l.lastRender = font.renderString(x + 3, y, l.text, 14F);
					l.lastRender.draw();
				}
			}
			y += 14F;
		}
	}

	public void addLine(String text) {
		StringBuilder line = new StringBuilder();
		StringBuilder current = new StringBuilder();
		float length = 0;
		float width = getWidth();
		for (int d = 0; d < text.length(); ++d) {
			char c = text.charAt(d);
			if (c == '{') {
				current.append(c);
				int indent = 0;
				for (d += 1; d < text.length(); ++d) {
					char s = text.charAt(d);
					if (s == '}') --indent;
					else if (s == '{') ++indent;
					current.append(s);
					if (indent < 0) break;
				}
				continue;
			} else if (c == ' ' || c == '\t' || c == '\n') {
				if (c != '\n') current.append(c);
				float siz = font.measureString(current.toString(), 14F).x;
				if (length + siz > width) {
					lines.add(new ChatLine(System.nanoTime(), line.toString()));
					line = new StringBuilder();
					length = 0;
				}
				length += siz;
				line.append(current);
				current = new StringBuilder();
				if (c == '\n') {
					lines.add(new ChatLine(System.nanoTime(), line.toString()));
					line = new StringBuilder();
				}
				continue;
			}
			current.append(c);
		}
		float siz = font.measureString(current.toString(), 14F).x;
		if (length + siz > width) {
			lines.add(new ChatLine(System.nanoTime(), line.toString()));
			line = new StringBuilder();
			length = 0;
		}
		length += siz;
		line.append(current);
		lines.add(new ChatLine(System.nanoTime(), line.toString()));
	}

	/**
	 * 
	 */
	public void clear() {
		lines.clear();
	}

}
