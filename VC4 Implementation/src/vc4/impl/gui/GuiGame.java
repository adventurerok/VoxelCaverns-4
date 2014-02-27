package vc4.impl.gui;

import java.awt.Rectangle;

import vc4.api.gui.Gui;
import vc4.api.vector.Vector2i;

public class GuiGame extends Gui {

	@Override
	public Vector2i getMinSize() {
		return new Vector2i(2 * SIDES_WIDTH + 480, TOP_WIDTH + SIDES_WIDTH + 360);
	}

	@Override
	public Vector2i getMaxSize() {
		return new Vector2i(2 * SIDES_WIDTH + 640, TOP_WIDTH + SIDES_WIDTH + 480);
	}

	@Override
	public Vector2i getDefaultSize() {
		return new Vector2i(2 * SIDES_WIDTH + 480, TOP_WIDTH + SIDES_WIDTH + 360);
	}

	@Override
	public Rectangle getDefaultBounds() {
		return new Rectangle(100, 100, 480, 360);
	}

	@Override
	public void close() {
		setVisible(false);
	}

}
