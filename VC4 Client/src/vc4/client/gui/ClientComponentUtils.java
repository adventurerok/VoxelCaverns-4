/**
 * 
 */
package vc4.client.gui;

import vc4.api.client.ClientGame;
import vc4.api.client.ClientWindow;
import vc4.api.font.FontRenderer;
import vc4.api.font.RenderedText;
import vc4.api.graphics.GLPrimitive;
import vc4.api.graphics.Graphics;
import vc4.api.graphics.OpenGL;
import vc4.api.gui.Border;
import vc4.api.gui.Button;
import vc4.api.gui.Component;
import vc4.api.gui.Component.ComponentUtils;
import vc4.api.gui.TextBox;
import vc4.api.gui.themed.ColorScheme;

import java.awt.*;

/**
 * @author paul
 * 
 */
public class ClientComponentUtils implements ComponentUtils {

	OpenGL gl;
	FontRenderer fnt14;
	FontRenderer fnt24;

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.ComponentUtils#attachBorder(java.awt.Rectangle, vc4.api.gui.Component, vc4.api.gui.Border)
	 */
	@Override
	public void attachBorder(Rectangle bounds, Component child, Border b) {
		Rectangle cBounds = child.getDefaultBounds();
		if (cBounds == null) return;
		int xWidth = bounds.x + bounds.width;
		int yHeight = bounds.y + bounds.height;
		int xHalf = bounds.x + bounds.width / 2;
		int yHalf = bounds.y + bounds.height / 2;
		int cxHalf = cBounds.width / 2;
		int cyHalf = cBounds.height / 2;

		switch (b) {
			case NONE:
				return;
			case FILL:
				child.setBounds(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));
				return;
			case CENTRE:
				child.setBounds(new Rectangle(xHalf - cxHalf, yHalf - cyHalf, cBounds.width, cBounds.height));
				return;
			case NORTHFILL:
				child.setBounds(new Rectangle(bounds.x, bounds.y, bounds.width, cBounds.height));
				break;
			case SOUTHFILL:
				child.setBounds(new Rectangle(bounds.x, yHeight - cBounds.height, bounds.width, cBounds.height));
				break;
			case WESTFILL:
				child.setBounds(new Rectangle(bounds.x, bounds.y, cBounds.width, bounds.height));
				break;
			case EASTFILL:
				child.setBounds(new Rectangle(xWidth - cBounds.width, bounds.y, cBounds.width, bounds.height));
				break;
			case NORTHCENTRE:
				child.setBounds(new Rectangle(xHalf - cxHalf, bounds.y, cBounds.width, cBounds.height));
				break;
			case SOUTHCENTRE:
				child.setBounds(new Rectangle(xHalf - cxHalf, bounds.y + bounds.height - cBounds.height, cBounds.width, cBounds.height));
				break;
			case WESTCENTRE:
				child.setBounds(new Rectangle(bounds.x, yHalf - cyHalf, cBounds.width, cBounds.height));
				break;
			case EASTCENTRE:
				child.setBounds(new Rectangle(bounds.x + bounds.width - cBounds.width, yHalf - cyHalf, cBounds.width, cBounds.height));
				break;
			case NORTH:
				child.setBounds(new Rectangle(bounds.x + cBounds.x, bounds.y, cBounds.width, cBounds.height));
				break;
			case SOUTH:
				child.setBounds(new Rectangle(bounds.x + cBounds.x, bounds.y + bounds.height - cBounds.height, cBounds.width, cBounds.height));
				break;
			case WEST:
				child.setBounds(new Rectangle(bounds.x, bounds.y + cBounds.y, cBounds.width, cBounds.height));
				break;
			case EAST:
				child.setBounds(new Rectangle(bounds.x + bounds.width - cBounds.width, bounds.y + cBounds.y, cBounds.width, cBounds.height));
				break;
			case NORTHWEST:
				child.setBounds(new Rectangle(bounds.x, bounds.y, cBounds.width, cBounds.height));
				break;
			case SOUTHWEST:
				child.setBounds(new Rectangle(bounds.x, bounds.y + bounds.height - cBounds.height, cBounds.width, cBounds.height));
				break;
			case NORTHEAST:
				child.setBounds(new Rectangle(bounds.x + bounds.width - cBounds.width, bounds.y, cBounds.width, cBounds.height));
				break;
			case SOUTHEAST:
				child.setBounds(new Rectangle(bounds.x + bounds.width - cBounds.width, bounds.y + bounds.height - cBounds.height, cBounds.width, cBounds.height));
				break;
			case NORTHSOUTH:
				child.setBounds(new Rectangle(bounds.x + cBounds.x, bounds.y, cBounds.width, bounds.height));
				break;
			case EASTWEST:
				child.setBounds(new Rectangle(bounds.x, bounds.y + cBounds.y, bounds.width, cBounds.height));
				break;
		}
	}

	/**
	 * 
	 */
	public ClientComponentUtils() {
		Component.setUtils(this);
		gl = Graphics.getOpenGL();
		fnt14 = FontRenderer.createFontRenderer("unispaced_14", 14);
		fnt24 = FontRenderer.createFontRenderer("unispaced_24", 24);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.Component.ComponentUtils#renderComponent(vc4.api.gui.Component)
	 */
	@Override
	public void renderComponent(Component c) {
		if (Button.class.isAssignableFrom(c.getClass())) {
			renderButton((Button) c);
		} else if (TextBox.class.isAssignableFrom(c.getClass())) {
			renderTextBox((TextBox) c);
		}
	}

	private void renderTextBox(TextBox c) {
		// TASK Auto-generated method stub

	}

	/**
	 * @param c
	 */
	private void renderButton(Button c) {
		gl.unbindShader();
		ColorScheme clr = getColorScheme();
		gl.begin(GLPrimitive.QUADS);
		gl.color(c.isHovering() ? clr.backgroundSelected : clr.backgroundNormal);
		callQuadForRectangle(c.getBounds(), 0, 0);
		gl.end();
		gl.begin(GLPrimitive.LINE_LOOP);
		gl.color(c.isHovering() ? clr.outlineSelected : clr.outlineNormal);
		callQuadForRectangle(c.getBounds(), 0, 0);
		gl.end();

		RenderedText old = c.getRenderedText();
		int textX = c.getX() + 5;
		int textY = (int) (c.getY() + c.getHeight() / 2 - (c.getFontSize() / 2F));
		if(old != null && old.getX() == textX && old.getY() == textY && old.getSize() == c.getFontSize() && old.getText().equals(c.getText())){
			old.draw();
		} else {
			FontRenderer fnt = c.getFontSize() < 18 ? fnt14 : fnt24;
			RenderedText text = fnt.renderString(c.getX() + 5, textY, c.getText(), c.getFontSize());
			fnt.resetStyles();
			text.draw();
			c.setRenderedText(text);
		}
	}

	private void callQuadForRectangle(Rectangle r, int xo, int yo) {
		gl.vertex(r.x + xo, r.y + yo);
		gl.vertex(r.x + xo + r.width, r.y + yo);
		gl.vertex(r.x + xo + r.width, r.y + yo + r.height);
		gl.vertex(r.x + xo, r.y + yo + r.height);
	}

	private ColorScheme getColorScheme() {
		ClientGame g = ClientWindow.getClientWindow().getGame();
		return g.getColorScheme(g.getColorSchemeSetting().getString());
	}

}
