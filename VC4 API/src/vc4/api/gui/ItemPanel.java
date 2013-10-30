package vc4.api.gui;

import java.awt.Rectangle;

import vc4.api.client.Client;
import vc4.api.client.ClientGame;
import vc4.api.container.Container;
import vc4.api.container.ContainerInventory;
import vc4.api.graphics.*;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.MouseSet;
import vc4.api.item.ItemStack;
import vc4.api.render.*;

public class ItemPanel extends Panel {

	private static OpenGL gl;

	Container container;
	ContainerInventory inventory;

	private int min, max = 0xffffff;

	public ItemPanel(Container cont) {
		if (gl == null) gl = Graphics.getOpenGL();
		container = cont;
	}

	@Override
	public void draw() {
		inventory = Client.getPlayer().getInventory();
		int px = getX();
		int py = getY();
		int sx = 0, sy = 0;
		int msx = 0;
		int msy = 0;
		ColorScheme scheme = getColorScheme();
		Graphics.getClientShaderManager().unbindShader();
		int max = Math.min(this.max, container.getSize());
		for (int d = min; d < max; ++d) {
			if (sx * 32 + 32 > getWidth()) {
				sx = 0;
				++sy;
			}
			if (sx > msx) msx = sx;
			if (sy * 32 + 32 > getHeight()) break;
			if (sy > msy) msy = sy;
			gl.begin(GLPrimative.LINE_LOOP);
			gl.color(scheme.outlineNormal);
			gl.vertex(px + sx * 32, py + sy * 32);
			gl.vertex(px + sx * 32 + 32, py + sy * 32);
			gl.vertex(px + sx * 32 + 32, py + sy * 32 + 32);
			gl.vertex(px + sx * 32, py + sy * 32 + 32);
			gl.end();
			++sx;
		}
		Graphics.getClientShaderManager().bindShader("texture");
		for (int x = msx; x > -1; --x) {
			for (int y = 0; y < msy + 1; ++y) {
				int at = min + (y * (msx + 1)) + x;
				ItemRenderer.renderItemStack(container.getItem(at), (8 + px + x * 32), (8 + py + y * 32));
			}
		}
		int hovPos = getHovering(msx, msy);
		if (Client.getGame().getHoveringComponent() != this) hovPos = -1;
		ItemStack hover = hovPos != -1 ? container.getItem(hovPos) : null;
		if (hover != null && inventory.getHeldItemStack() == null) {
			try {
				ItemTooltipRenderer.renderTooltip(hover, ToolTipType.INVENTORY);

			} catch (Exception e) {
			}
		}
	}

	@Override
	public void update() {
		inventory = Client.getPlayer().getInventory();
		MouseSet mice = Client.getGame().getMouseSet();
		boolean left = mice.buttonPressed(0);
		boolean right = mice.buttonPressed(1);
		if (!left && !right) return;

		int max = Math.min(this.max, container.getSize());
		for (int d = min; d < max; ++d) {
			if (container.getItem(d) != null && !container.getItem(d).exists()) {
				container.setItem(d, null);
			}
		}

		int sx = 0, sy = 0, msx = 0, msy = 0;
		for (int d = min; d < max; ++d) {
			if (sx * 32 + 32 > getWidth()) {
				sx = 0;
				++sy;
			}
			if (sx > msx) msx = sx;
			if (sy * 32 + 32 > getHeight()) break;
			if (sy > msy) msy = sy;
			++sx;
		}
		int hover = getHovering(msx, msy);
		if (Client.getGame().getHoveringComponent() != this) hover = -1;
		if(hover != -1){
			if (left) inventory.setHeldItemStack(container.leftClick(hover, inventory.getHeldItemStack()));
			else inventory.setHeldItemStack(container.rightClick(hover, inventory.getHeldItemStack()));
		}

	}

	public int getHovering(int msx, int msy) {
		MouseSet mice = Client.getGame().getMouseSet();
		Rectangle mouse = mice.getMouseRectangle();
		int x = getX();
		int y = getY();
		for (int sx = 0; sx < msx + 1; sx++) {
			for (int sy = 0; sy < msy + 1; sy++) {
				int at = min + (sy * (msx + 1)) + sx;
				Rectangle slot = new Rectangle(x + sx * 32, y + sy * 32, 32, 32);
				if (mouse.intersects(slot)) { return at; }
			}
		}
		return -1;
	}

	private static ColorScheme getColorScheme() {
		ClientGame g = Client.getGame();
		return g.getColorScheme(g.getColorSchemeSetting().getString());
	}

}
