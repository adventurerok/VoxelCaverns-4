package vc4.impl.gui;

import java.awt.Rectangle;

import vc4.api.client.ClientWindow;
import vc4.api.gui.Gui;
import vc4.api.vector.Vector2i;

public class GuiArmour extends Gui {

	@Override
	public Vector2i getMinSize() {
		return new Vector2i(2 * SIDES_WIDTH + 354, TOP_WIDTH + SIDES_WIDTH + 354);
	}

	@Override
	public Vector2i getMaxSize() {
		return new Vector2i(2 * SIDES_WIDTH + 354, TOP_WIDTH + SIDES_WIDTH + 354);
	}

	@Override
	public Vector2i getDefaultSize() {
		return new Vector2i(2 * SIDES_WIDTH + 354, TOP_WIDTH + SIDES_WIDTH + 354);
	}

	@Override
	public Rectangle getDefaultBounds() {
		Vector2i size = getDefaultSize();
		int w = ClientWindow.getClientWindow().getWidth();
		return new Rectangle(w / 2 - size.x / 2, 100, size.x, size.y);
	}

	@Override
	public void close() {
		setVisible(false);

	}

}
