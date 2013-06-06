package vc4.api.container;

import java.util.*;

import org.jnbt.CompoundTag;
import org.jnbt.NibbleTag;

import vc4.api.item.ItemStack;
import vc4.api.world.World;

public class ContainerInventory extends Container {

	@Override
	protected void writeExtraData(World world, CompoundTag tag) {
		tag.addTag(new NibbleTag("selected", (byte) selected));
		CompoundTag held = new CompoundTag("held");
		ItemStack.write(world, getHeldItemStack(), held);
		tag.addTag(held);
	}

	@Override
	protected void readExtraData(World world, CompoundTag tag) {
		selected = tag.getNibbleTag("selected").getValue();
		setHeldItemStack(ItemStack.read(world, tag.getCompoundTag("held")));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3810801114181989994L;
	private int selected = 0;
	private ItemStack _held;
	private float scroll = 0.5F;

	public float getScroll() {
		return scroll;
	}

	public void setScroll(float scroll) {
		this.scroll = scroll;
		this.selected = (int) scroll;
	}

	public ContainerInventory() {
		super(44);
	}
	
	@Override
	public String getGuiName() {
		return "inventory";
	}

	public ItemStack getSelectedStack() {
		return slots[selected];
	}

	public int getSelectedIndex() {
		return selected;
	}

	public void setSelectedStack(ItemStack sel) {
		setItem(getSelectedIndex(), sel);
	}

	public void setSelectedIndex(int i) {
		selected = i;
		scroll = i + 0.5F;
	}

	public ItemStack getHeldItemStack() {
		return _held;
	}

	public void setHeldItemStack(ItemStack held) {
		_held = held;
	}

	public void shiftItemsUp() {
		int length = getSize();
		ItemStack[] n = new ItemStack[length];
		for (int dofor = 0; dofor < length; ++dofor) {
			int at = dofor + 11;
			if (at >= length) at -= length;
			n[at] = getItem(dofor);
		}
		setAllItems(n);
	}

	public void shiftItemsDown() {
		int length = getSize();
		ItemStack[] n = new ItemStack[length];
		for (int dofor = 0; dofor < length; ++dofor) {
			int at = dofor - 11;
			if (at < 0) at += length;
			n[at] = getItem(dofor);
		}
		setAllItems(n);
	}

	public void shiftSelectedUp() {
		int length = getSize();
		ItemStack[] n = new ItemStack[length];
		for (int dofor = 0; dofor < length; ++dofor) {
			int sel = dofor % 11;
			if (sel == getSelectedIndex()) {
				int at = dofor + 11;
				if (at >= length) at -= length;
				n[at] = getItem(dofor);
			} else n[dofor] = getItem(dofor);
		}
		setAllItems(n);

	}

	public void shiftSelectedDown() {
		int length = getSize();
		ItemStack[] n = new ItemStack[length];
		for (int dofor = 0; dofor < length; ++dofor) {
			int sel = dofor % 11;
			if (sel == getSelectedIndex()) {
				int at = dofor - 11;
				if (at < 0) at += length;
				n[at] = getItem(dofor);
			} else n[dofor] = getItem(dofor);
		}
		setAllItems(n);

	}


	/**
	 * 
	 */
	public void sort() {
		ArrayList<ItemStack> items = new ArrayList<>();
		for(int d = 0; d < getSize(); ++d){
			ItemStack item = getItem(d);
			if(item == null || !item.checkIsNotEmpty()) continue;
			int ind = items.indexOf(item);
			if(ind == -1){
				items.add(item);
				continue;
			} else {
				ItemStack c = items.get(ind);
				item = c.combineItemStack(item);
				if(item != null && item.checkIsNotEmpty()){
					items.set(ind, item);
				} else {
					setItem(d, null);
				}
			}
		}
	}

	@Override
	public String getName() {
		return "inventory";
	}
}
