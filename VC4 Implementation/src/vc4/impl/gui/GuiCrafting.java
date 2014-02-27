package vc4.impl.gui;

import java.awt.Rectangle;

import vc4.api.client.ClientWindow;
import vc4.api.gui.Gui;
import vc4.api.vector.Vector2i;
import vc4.impl.gui.inner.InnerCraftingGui;

public class GuiCrafting extends Gui {

	public GuiCrafting() {
		innerGui = new InnerCraftingGui();
	}

	@Override
	public Vector2i getMinSize() {
		return new Vector2i(32 * 11 + 2 * SIDES_WIDTH, 32 * 4 + TOP_WIDTH + SIDES_WIDTH);
	}

	@Override
	public Vector2i getMaxSize() {
		return new Vector2i(32 * 15 + 2 * SIDES_WIDTH, 32 * 6 + TOP_WIDTH + SIDES_WIDTH);
	}

	@Override
	public Vector2i getDefaultSize() {
		return new Vector2i(32 * 11 + 2 * SIDES_WIDTH, 32 * 4 + TOP_WIDTH + SIDES_WIDTH);
	}

	@Override
	public Rectangle getDefaultBounds() {
		Vector2i size = getDefaultSize();
		int w = ClientWindow.getClientWindow().getWidth();
		int h = ClientWindow.getClientWindow().getHeight();
		return new Rectangle(w / 2 - size.x / 2, h - 384 - size.y, size.x, size.y);
	}

	@Override
	public void close() {
		setVisible(false);
	}

}
