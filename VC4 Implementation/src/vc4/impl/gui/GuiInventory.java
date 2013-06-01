package vc4.impl.gui;

import java.awt.Rectangle;

import vc4.api.client.Client;
import vc4.api.client.ClientGame;
import vc4.api.container.ContainerInventory;
import vc4.api.graphics.*;
import vc4.api.gui.*;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.MouseSet;
import vc4.api.item.ItemStack;
import vc4.api.render.*;

public class GuiInventory extends Component{

	public ContainerInventory inventory;

	public GuiInventory() {
		setResizer(new BorderResizer(Border.SOUTHCENTRE));
	}
	
	private static ColorScheme getColorScheme(){
		ClientGame g = Client.getGame();
		return g.getColorScheme(g.getColorSchemeSetting().getString());
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#getDefaultBounds()
	 */
	@Override
	public Rectangle getDefaultBounds() {
		return new Rectangle(352, 128);
	}
	

	@Override
	public void draw() {
		int px = getX();
		int py = getY();
		inventory = Client.getGame().getPlayer().getInventory();
		OpenGL gl = Graphics.getOpenGL();
		Graphics.getClientShaderManager().unbindShader();
		for (int x = 10; x > -1; x--) {
			for (int y = 0; y < 4; y++) {
				if (!inventory.isOpen() && y < 3) continue;
				int at = ((3 - y) * 11) + x;
				gl.begin(GLPrimative.QUADS);
				if (inventory.getSelectedIndex() == at) gl.color(getColorScheme().backgroundSelected);
				else gl.color(getColorScheme().backgroundNormal);
				gl.vertex(px + x * 32, py + y * 32);
				gl.vertex(px + x * 32 + 32, py + y * 32);
				gl.vertex(px + x * 32 + 32, py + y * 32 + 32);
				gl.vertex(px + x * 32, py + y * 32 + 32);
				gl.end();
				gl.begin(GLPrimative.LINE_LOOP);
				if (inventory.getSelectedIndex() == at) gl.color(getColorScheme().outlineSelected);
				else gl.color(getColorScheme().outlineNormal);
				gl.vertex(px + x * 32, py + y * 32);
				gl.vertex(px + x * 32 + 32, py + y * 32);
				gl.vertex(px + x * 32 + 32, py + y * 32 + 32);
				gl.vertex(px + x * 32, py + y * 32 + 32);
				gl.end();
			}
		}
		Graphics.getClientShaderManager().bindShader("texture");
		for (int x = 10; x > -1; x--) {
			for (int y = 0; y < 4; y++) {
				if (!inventory.isOpen() && y < 3) continue;
				int at = ((3 - y) * 11) + x;
				ItemRenderer.renderItemStack(inventory.getItem(at), (8 + px + x * 32), (8 + py + y * 32));
			}
		}
		if (inventory.getHeldItemStack() != null) {
			OverlayRenderer.addRender(new OverlayRender() {

				@Override
				public void draw() {
					Graphics.getClientShaderManager().bindShader("font");
					MouseSet mice = Client.getGame().getMouseSet();
					ItemRenderer.renderItemStack(inventory.getHeldItemStack(), mice.getX() + 13, mice.getY() + 21);

				}
			});

		}
		int hovPos = getHovering();
		ItemStack hover = hovPos != -1 ? inventory.getItem(hovPos) : null;
		if (hover != null && inventory.getHeldItemStack() == null) {
			try {
				ItemTooltipRenderer.renderTooltip(hover, ToolTipType.INVENTORY);

			} catch (Exception e) {
			}
		}
	}

	@Override
	public void update() {
		//draw();
		if (!Client.getGame().isPaused()) return;
		inventory = Client.getGame().getPlayer().getInventory();
		if (inventory == null) return;
		if (inventory.getHeldItemStack() != null) {
			if (!inventory.getHeldItemStack().checkIsNotEmpty()) inventory.setHeldItemStack(null);
		}
		MouseSet mice = Client.getGame().getMouseSet();
		boolean left = mice.buttonPressed(0);
		boolean right = mice.buttonPressed(1);
		if(!left && !right) return;
		int x = (int) getBounds().getX();
		int y = (int) getBounds().getY();
		Rectangle mouse = mice.getMouseRectangle();

		if (!inventory.isOpen()) {
			for (int sx = 0; sx < 11; sx++) {
				int sy = 3;
				int at = ((3 - sy) * 11) + sx;
				Rectangle slot = new Rectangle(x + sx * 32, y + sy * 32, 32, 32);
				if (mouse.intersects(slot) && left) {
					inventory.setScroll(at + 0.5F);
				}

			}
			return;
		}

		for (int sx = 0; sx < 11; sx++) {
			for (int sy = 0; sy < 4; sy++) {
				int at = ((3 - sy) * 11) + sx;
				// int at = sy * 11 + sx;
				if (inventory.getItem(at) != null) {
					if (!inventory.getItem(at).checkIsNotEmpty()) {
						inventory.setItem(at, null);
					}
				}
				Rectangle slot = new Rectangle(x + sx * 32, y + sy * 32, 32, 32);
				if (mouse.intersects(slot)) {
					//Container.setContainerGui(this);
					try {
						if (left) inventory.setHeldItemStack(inventory.leftClick(at, inventory.getHeldItemStack()));
						else inventory.setHeldItemStack(inventory.rightClick(at, inventory.getHeldItemStack()));

					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
	}

//	/**
//	 * @param item
//	 * @return The remaining items
//	 */
//	@Override
//	public ItemStack controlClick(ItemStack item, int slot) {
//		EntityPlayer p = SingleplayerUtils.getPlayer();
//		if (p.getCurrentTab() == 2) {
//			ContainerChest c = p.getActiveContainer().chest;
//			if (c != null) { return c.addItemStack(item); }
//		}
//		return item;
//	}

	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#isClickable()
	 */
	@Override
	public boolean isClickable() {
		return false;
	}

	public int getHovering() {
		MouseSet mice = Client.getGame().getMouseSet();
		Rectangle mouse = mice.getMouseRectangle();
		int x = getX();
		int y = getY();
		for (int sx = 0; sx < 11; sx++) {
			for (int sy = 0; sy < 4; sy++) {
				if (!inventory.isOpen() && sy < 3) continue;
				int at = ((3 - sy) * 11) + sx;
				Rectangle slot = new Rectangle(x + sx * 32, y + sy * 32, 32, 32);
				if (mouse.intersects(slot)) { return at; }
			}
		}
		return -1;
	}

}
