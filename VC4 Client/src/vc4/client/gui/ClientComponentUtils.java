/**
 * 
 */
package vc4.client.gui;

import java.awt.Rectangle;

import vc4.api.client.ClientGame;
import vc4.api.client.ClientWindow;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.*;
import vc4.api.gui.Component.ComponentUtils;
import vc4.api.gui.themed.ColorScheme;

import static vc4.api.gui.Border.*;

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
		Rectangle cbounds = child.getDefaultBounds();
		if (cbounds == null) return;
		int xwidth = bounds.x + bounds.width;
		int yheight = bounds.y + bounds.height;
		int xhalf = bounds.x + bounds.width / 2;
		int yhalf = bounds.y + bounds.height / 2;
		int cxhalf = cbounds.width / 2;
		int cyhalf = cbounds.height / 2;
		if (b == NONE) return;
		if (b == FILL) {
			child.setBounds(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));
			return;
		}
		if (b == CENTRE) {
			child.setBounds(new Rectangle(xhalf - cxhalf, yhalf - cyhalf, cbounds.width, cbounds.height));
			return;
		}

		if (b == NORTHFILL) {
			child.setBounds(new Rectangle(bounds.x, bounds.y, bounds.width, cbounds.height));
		} else if (b == SOUTHFILL) {
			child.setBounds(new Rectangle(bounds.x, yheight - cbounds.height, bounds.width, cbounds.height));
		} else if (b == WESTFILL) {
			child.setBounds(new Rectangle(bounds.x, bounds.y, cbounds.width, bounds.height));
		} else if (b == EASTFILL) {
			child.setBounds(new Rectangle(xwidth - cbounds.width, bounds.y, cbounds.width, bounds.height));
		} else if (b == NORTHCENTRE) {
			child.setBounds(new Rectangle(xhalf - cxhalf, bounds.y, cbounds.width, cbounds.height));
		} else if (b == SOUTHCENTRE) {
			child.setBounds(new Rectangle(xhalf - cxhalf, bounds.y + bounds.height - cbounds.height, cbounds.width, cbounds.height));
		} else if (b == WESTCENTRE) {
			child.setBounds(new Rectangle(bounds.x, yhalf - cyhalf, cbounds.width, cbounds.height));
		} else if (b == EASTCENTRE) {
			child.setBounds(new Rectangle(bounds.x + bounds.width - cbounds.width, yhalf - cyhalf, cbounds.width, cbounds.height));
		} else if (b == NORTH) {
			child.setBounds(new Rectangle(bounds.x + cbounds.x, bounds.y, cbounds.width, cbounds.height));
		} else if (b == SOUTH) {
			child.setBounds(new Rectangle(bounds.x + cbounds.x, bounds.y + bounds.height - cbounds.height, cbounds.width, cbounds.height));
		} else if (b == WEST) {
			child.setBounds(new Rectangle(bounds.x, bounds.y + cbounds.y, cbounds.width, cbounds.height));
		} else if (b == EAST) {
			child.setBounds(new Rectangle(bounds.x + bounds.width - cbounds.width, bounds.y + cbounds.y, cbounds.width, cbounds.height));
		} else if (b == NORTHWEST) {
			child.setBounds(new Rectangle(bounds.x, bounds.y, cbounds.width, cbounds.height));
		} else if (b == SOUTHWEST) {
			child.setBounds(new Rectangle(bounds.x, bounds.y + bounds.height - cbounds.height, cbounds.width, cbounds.height));
		} else if (b == NORTHEAST) {
			child.setBounds(new Rectangle(bounds.x + bounds.width - cbounds.width, bounds.y, cbounds.width, cbounds.height));
		} else if (b == SOUTHEAST) {
			child.setBounds(new Rectangle(bounds.x + bounds.width - cbounds.width, bounds.y + bounds.height - cbounds.height, cbounds.width, cbounds.height));
		} else if (b == NORTHSOUTH) {
			child.setBounds(new Rectangle(bounds.x + cbounds.x, bounds.y, cbounds.width, bounds.height));
		} else if (b == EASTWEST) {
			child.setBounds(new Rectangle(bounds.x, bounds.y + cbounds.y, bounds.width, cbounds.height));
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
		ColorScheme clr = getColorScheme();
		gl.begin(GLPrimative.QUADS);
		gl.color(c.isHovering() ? clr.backgroundSelected : clr.backgroundNormal);
		callQuadForRectangle(c.getBounds(), 0, 0);
		gl.end();
		gl.begin(GLPrimative.LINE_LOOP);
		gl.color(c.isHovering() ? clr.outlineSelected : clr.outlineNormal);
		callQuadForRectangle(c.getBounds(), 0, 0);
		gl.end();
		int textY = (int) (c.getY() + c.getHeight() / 2 - (c.getFontSize() / 2F));
		FontRenderer fnt = c.getFontSize() < 18 ? fnt14 : fnt24;
		fnt.renderString(c.getX() + 5, textY, c.getText(), c.getFontSize());
		fnt.resetStyles();
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
