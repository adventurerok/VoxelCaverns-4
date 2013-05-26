package vc4.impl.gui.inner;

import java.awt.Rectangle;
import java.util.*;

import vc4.api.Resources;
import vc4.api.client.Client;
import vc4.api.container.ContainerInventory;
import vc4.api.crafting.CraftingManager;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.*;
import vc4.api.gui.Panel;
import vc4.api.gui.themed.ColorScheme;
import vc4.api.input.*;
import vc4.api.item.ItemStack;
import vc4.api.render.*;

public class InnerCraftingGui extends Panel {

	private static OpenGL gl;

	public FontRenderer font = FontRenderer.createFontRenderer("unispaced_14", 14);
	public int amountOfSlots = 2;
	public ItemStack[] input = new ItemStack[amountOfSlots];
	public int[] inputAmounts = new int[amountOfSlots];
	public ItemStack[] output = new ItemStack[1];
	public ItemStack[] lastCraftingSorted;
	private ContainerInventory inventory;

	public short[] currentItem = new short[0];

	public InnerCraftingGui() {
		if (gl == null) gl = Graphics.getClientOpenGL();
	}

	@Override
	public void draw() {
		int start = (getWidth() / 2 - (amountOfSlots + 1) * 16 - 24);
		int px = getX();
		int py = getY();
		gl.unbindShader();
		ColorScheme scheme = Client.getGame().getCurrentColorScheme();
		for (int x = amountOfSlots - 1; x > -1; x--) {
			gl.begin(GLPrimative.LINE_LOOP);
			gl.color(scheme.outlineNormal);
			gl.vertex(start + px + x * 32, py + 12);
			gl.vertex(start + px + x * 32 + 32, py + 12);
			gl.vertex(start + px + x * 32 + 32, py + 32 + 12);
			gl.vertex(start + px + x * 32, py + 32 + 12);
			gl.end();
		}
		gl.bindShader("texture");
		for (int x = amountOfSlots - 1; x > -1; x--) {
			ItemRenderer.renderItemStack(input[x], start + px + x * 32 + 8, py + 12 + 8);
			int stringMinus = (int) (font.measureString(inputAmounts[x] + "", 14).x / 2);
			font.renderString(start + px + x * 32 + 16 - stringMinus, py + 42, inputAmounts[x] + "");
		}
		if (output != null && output.length > 0) {
			Rectangle mouse = Client.getGame().getMouseSet().getMouseRectangle();
			int start1 = (px + 352 - 24 - ((output.length / 4) * 24));
			for (int dofor = 0; dofor < output.length; dofor++) {
				if (output[dofor] == null || !output[dofor].checkIsNotEmpty()) continue;
				int ax = dofor / 4;
				int ay = dofor % 4;
				ItemRenderer.renderItemStack(output[dofor], start1 + (ax * 24), py + 8 + (24 * ay));
				Rectangle outResult = new Rectangle(start1 + (ax * 24), py + 8 + (24 * ay), 16, 16);
				if (mouse.intersects(outResult)) {
					ItemTooltipRenderer.renderTooltip(output[dofor], ToolTipType.INVENTORY);
				}
			}
		}
		gl.bindShader("texture");
		Resources.getAnimatedTexture("crafting").bind();
		gl.begin(GLPrimative.QUADS);
		gl.color(1F, 1F, 1F, 1F);
		int x = px;
		int y = py + getHeight();
		for (int dofor = 0; dofor < currentItem.length; ++dofor) {
			int ix = dofor % 14;
			int iy = dofor / 14;
			int tex = CraftingManager.getToolIcon(currentItem[dofor]);
			gl.texCoord(0, 0, tex);
			gl.vertex(x + ix * 24, y - iy * 24 - 24);
			gl.texCoord(1, 0, tex);
			gl.vertex(x + ix * 24 + 24, y - iy * 24 - 24);
			gl.texCoord(1, 1, tex);
			gl.vertex(x + ix * 24 + 24, y - iy * 24);
			gl.texCoord(0, 1, tex);
			gl.vertex(x + ix * 24, y - iy * 24);
		}
		gl.end();
		gl.unbindShader();
	}

	public boolean handleClicks() {
		inventory = Client.getPlayer().getInventory();
		if (inventory == null) return false;
		if (inventory.getHeldItemStack() != null) {
			if (!inventory.getHeldItemStack().checkIsNotEmpty()) inventory.setHeldItemStack(null);
		}

		MouseSet mice = Client.getGame().getMouseSet();
		if (!mice.buttonPressed(0) && !mice.buttonPressed(1)) return false;
		boolean left = mice.buttonPressed(0);
		int x = getX();
		int y = getY();
		Rectangle mouse = mice.getMouseRectangle();

		int start = (getWidth() / 2 - (amountOfSlots + 1) * 16 - 24);
		for (int sx = 0; sx < amountOfSlots; sx++) {

			// int at = sy * 11 + sx;
			if (input[sx] != null) {
				if (!input[sx].checkIsNotEmpty()) {
					input[sx] = null;
				}
			}
			Rectangle slot = new Rectangle(x + start + sx * 32, y + 12, 32, 32);
			if (mouse.intersects(slot)) {
				try {
					if (left) {
						// Left button on slot
						handleLeftClick(sx);
						return true;
					} else {
						// Right button on slot
						handleRightClick(sx);
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

		}
		if (inventory.getHeldItemStack() != null) if (!inventory.getHeldItemStack().checkIsNotEmpty()) inventory.setHeldItemStack(null);
		if (output != null && output.length > 0) {
			int start1 = (x + 352 - 24 - ((output.length / 4) * 24));
			for (int dofor = 0; dofor < output.length; dofor++) {
				int ax = dofor / 4;
				int ay = dofor % 4;

				Rectangle outResult = new Rectangle(start1 + (ax * 24), y + 8 + (24 * ay), 16, 16);
				if (outResult.intersects(mouse)) {
					if (left) {
						return takenFromCrafting(dofor);
					} else {
						boolean done = false;
						while (done = takenFromCrafting(dofor)) {
							lastCraftingSorted = switchForInput();
							output = CraftingManager.tryCraft(currentItem, combineInput(lastCraftingSorted));
						}
						return done;
					}

				}
			}
		}
		return false;
	}

	private void handleLeftClick(int slot) {
		if (Input.getClientKeyboard().isKeyDown(Key.CONTROL)) {
			inputAmounts[slot]++;
			if (input[slot] != null) {
				if (inputAmounts[slot] > input[slot].getAmount()) inputAmounts[slot]--;
			}
			if (inputAmounts[slot] > 99) inputAmounts[slot] = 99;
			return;
		}
		if (input[slot] == null) {
			if (inventory.getHeldItemStack() != null) {
				input[slot] = inventory.getHeldItemStack().clone();
				inventory.setHeldItemStack(null);
			}
		} else {
			if (inventory.getHeldItemStack() != null) {
				inventory.setHeldItemStack(input[slot].combineItemStack(inventory.getHeldItemStack()));
			} else {
				inventory.setHeldItemStack(input[slot].clone());
				input[slot] = null;

			}
		}
	}

	/**
	 * Handles a right click on the inventory slot numbered
	 * 
	 * @param slot
	 *            The slot in which to handle the click
	 */
	private void handleRightClick(int slot) {
		if (Input.getClientKeyboard().isKeyDown(Key.CONTROL)) {
			inputAmounts[slot]--;
			if (inputAmounts[slot] < 0) inputAmounts[slot] = 0;
			return;
		}
		if (input[slot] == null) {
			if (inventory.getHeldItemStack() != null) {
				input[slot] = inventory.getHeldItemStack().clone();
				input[slot].setAmount(1);
				inventory.getHeldItemStack().decrementAmount();
			}
		} else {
			if (inventory.getHeldItemStack() != null) {
				if (inventory.getHeldItemStack().equals(input[slot])) {
					if (input[slot].incrementAmount()) {
						inventory.getHeldItemStack().decrementAmount();
					}
				}
			} else {
				int total = input[slot].getAmount();
				int a = total / 2;
				int b = total - a;
				inventory.setHeldItemStack(input[slot].clone());
				inventory.getHeldItemStack().setAmount(b);
				input[slot].setAmount(a);

			}
		}
	}

	public ItemStack[] combineInput(ItemStack... stacks) {
		for (int pos = 0; pos < stacks.length; pos++) {
			if (stacks[pos] == null) continue;
			for (int dofor = 0; dofor < stacks.length; dofor++) {
				if (pos == dofor) continue;
				if (stacks[pos].equals(stacks[dofor])) {
					stacks[pos].setAmount(stacks[pos].getAmount() + stacks[dofor].getAmount());
					stacks[dofor] = null;
				}
			}
		}
		List<ItemStack> newStackList = new ArrayList<ItemStack>();
		for (int dofor = 0; dofor < stacks.length; dofor++) {
			if (stacks[dofor] != null) newStackList.add(stacks[dofor]);
		}
		stacks = newStackList.toArray(new ItemStack[newStackList.size()]);
		Arrays.sort(stacks);
		return stacks;
	}

	public boolean takenFromCrafting(int num) {
		if (output == null) return false;
		if (output.length < num) return false;
		ItemStack itemOut = output[num];
		if (inventory.getHeldItemStack() == null) inventory.setHeldItemStack(itemOut.clone());
		else {
			if (inventory.getHeldItemStack().equals(itemOut)) {
				ItemStack inHand = inventory.getHeldItemStack().clone();
				ItemStack newOut = inHand.combineItemStack(itemOut);
				if (newOut != null) {
					if (newOut.checkIsNotEmpty()) return false;
				}
				inventory.setHeldItemStack(inHand);

			} else return false;
		}
		for (int dofor = 0; dofor < amountOfSlots; dofor++) {
			if (input[dofor] == null) continue;
			input[dofor].setAmount(input[dofor].getAmount() - inputAmounts[dofor]);
			if (inputAmounts[dofor] > input[dofor].getAmount()) inputAmounts[dofor] = input[dofor].getAmount();
			if (!input[dofor].checkIsNotEmpty()) input[dofor] = null;
		}
		return true;

	}

	@Override
	public void update() {
		if (Client.getGame().getPlayer().getCraftingTable().slots != amountOfSlots) {
			setAmountOfSlots(Client.getPlayer().getCraftingTable().slots);
		}
		setItem(Client.getPlayer().getCraftingTable().items);
		if (handleClicks()) {
			lastCraftingSorted = switchForInput();
			output = CraftingManager.tryCraft(currentItem, combineInput(lastCraftingSorted));
		}

	}

	public void setAmountOfSlots(int amount) {
		if (amount > 8) return;
		int old = amountOfSlots;
		amountOfSlots = amount;
		if (amountOfSlots < old) {
			for (int dofor = amountOfSlots; dofor < old; ++dofor) {
				Client.getPlayer().dropItem(input[dofor]);
			}
		}
		input = Arrays.copyOf(input, amount);
		inputAmounts = Arrays.copyOf(inputAmounts, amount);
		for (int dofor = old; dofor < amountOfSlots; dofor++) {
			inputAmounts[dofor] = 1;
		}
		lastCraftingSorted = switchForInput();
		output = CraftingManager.tryCraft(currentItem, combineInput(lastCraftingSorted));
	}

	public ItemStack[] switchForInput() {
		for (int dofor = 0; dofor < amountOfSlots; ++dofor) {
			if (input[dofor] != null && input[dofor].checkIsNotEmpty()) {
				if (input[dofor].getAmount() < inputAmounts[dofor]) inputAmounts[dofor] = input[dofor].getAmount();
			} else inputAmounts[dofor] = 1;
		}
		ItemStack result[] = new ItemStack[amountOfSlots];
		for (int dofor = 0; dofor < amountOfSlots; dofor++) {
			if (input[dofor] != null) result[dofor] = new ItemStack(input[dofor].getId(), input[dofor].getDamage(), inputAmounts[dofor]);
			else result[dofor] = null;
		}
		return result;
	}

	public void setItem(short[] item) {
		if (Arrays.equals(item, currentItem)) return;
		currentItem = item;
		lastCraftingSorted = switchForInput();
		output = CraftingManager.tryCraft(currentItem, combineInput(lastCraftingSorted));
	}

}
