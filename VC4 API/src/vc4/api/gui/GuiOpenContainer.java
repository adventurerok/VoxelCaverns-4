package vc4.api.gui;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import vc4.api.block.OpenContainer;
import vc4.api.client.ClientWindow;
import vc4.api.logging.Logger;
import vc4.api.vector.Vector2i;

public class GuiOpenContainer extends Gui {

	private static HashMap<String, Class<? extends GuiOpenContainer>> types = new HashMap<>();

	public static void addContainerGui(String name, Class<? extends GuiOpenContainer> clz) {
		types.put(name, clz);
	}

	public static GuiOpenContainer createContainerGui(OpenContainer container) {
		Class<? extends GuiOpenContainer> c = types.get(container.entity.getContainer().getGuiName());
		try {
			return c.getConstructor(OpenContainer.class).newInstance(container);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Logger.getLogger(GuiOpenContainer.class).warning("Failed to create container GUI", e);
		}
		return null;
	}

	protected OpenContainer container;

	public GuiOpenContainer(OpenContainer cont) {
		container = cont;
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
		container.valid = false;
		getParent().remove(this);
	}

}
