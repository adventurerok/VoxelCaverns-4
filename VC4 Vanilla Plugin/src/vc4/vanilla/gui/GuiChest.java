package vc4.vanilla.gui;

import java.awt.Rectangle;

import vc4.api.block.OpenContainer;
import vc4.api.gui.GuiOpenContainer;
import vc4.api.gui.ItemPanel;
import vc4.api.vector.Vector2i;

public class GuiChest extends GuiOpenContainer {

	public GuiChest(OpenContainer cont) {
		super(cont);
		innerGui = new ItemPanel(cont.entity.getContainer());
	}
	
	@Override
	public Vector2i getMaxSize() {
		int w = 2 * SIDES_WIDTH;
		int h = TOP_WIDTH + SIDES_WIDTH;
		w += 32 * 20;
		h += 32 * 20;
		return new Vector2i(w, h);
	}
	
	@Override
	public Vector2i getMinSize() {
		int w = 2 * SIDES_WIDTH;
		int h = TOP_WIDTH + SIDES_WIDTH;
		w += 32 * 1;
		h += 32 * 1;
		return new Vector2i(w, h);
	}
	
	@Override
	public Vector2i getDefaultSize() {
		int w = 2 * SIDES_WIDTH;
		int h = TOP_WIDTH + SIDES_WIDTH;
		int a = container.entity.getContainer().getSize() < 90 ? 11 : 13;
		w += 32 * a;
		h += 32 * (container.entity.getContainer().getSize() / a);
		return new Vector2i(w, h);
	}
	
	@Override
	public Rectangle getDefaultBounds() {
		Vector2i size = getDefaultSize();
		return new Rectangle(100, 100, size.x, size.y);
	}

}
