package vc4.api.container;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.jnbt.*;

import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.logging.Logger;
import vc4.api.world.World;

public abstract class Container implements IContainer, Iterable<ItemStack>, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1914854850244654107L;
	// private static IContainerGui containerGui;
	protected boolean isOpen = false;
	protected ItemStack[] slots;
	protected boolean modified;

	public static HashMap<String, Constructor<? extends Container>> types = new HashMap<>();

	static {
		registerContainer("inventory", ContainerInventory.class);
		registerContainer("creative", ContainerCreative.class);
		registerContainer("items", ContainerItems.class);
	}

	public static void registerContainer(String name, Class<? extends Container> c) {
		try {
			types.put(name, c.getConstructor());
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(Container.class).warning("Exception occured", e);
		}
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isModified() {
		return modified;
	}

	protected Container() {
	}

	public String getGuiName() {
		return null;
	}

	protected Container(int slots) {
		this.slots = new ItemStack[slots];
	}

	protected Container(ItemStack... items) {
		slots = items;
	}

	public void clear() {
		for (int dofor = 0; dofor < slots.length; ++dofor) {
			slots[dofor] = null;
		}
		setModified(true);
	}

	@Override
	public ItemStack addItemStack(ItemStack stack) {
		if (stack == null || !stack.exists()) return null;
		stack = stack.clone();
		for (int dofor = 0; dofor < slots.length; dofor++) {
			if (stack.equals(slots[dofor])) {
				stack = slots[dofor].combineItemStack(stack);
				if (stack == null || !stack.exists()) {
					stack = null;
					setModified(true);
					return stack;
				}
			}
		}
		for (int dofor = 0; dofor < slots.length; dofor++) {
			if (slots[dofor] == null || !slots[dofor].exists()) {
				slots[dofor] = stack;
				stack = null;
				setModified(true);
				return stack;
			}
		}
		setModified(true);
		return stack;
	}

	public ItemStack addItemStack(ItemStack stack, int min, int max) {
		if (stack == null || !stack.exists()) return null;
		stack = stack.clone();
		for (int dofor = min; dofor < max; dofor++) {
			if (stack.equals(slots[dofor])) {
				stack = slots[dofor].combineItemStack(stack);
				if (stack == null || !stack.exists()) {
					stack = null;
					setModified(true);
					return stack;
				}
			}
		}
		for (int dofor = min; dofor < max; dofor++) {
			if (slots[dofor] == null || !slots[dofor].exists()) {
				slots[dofor] = stack;
				stack = null;
				setModified(true);
				return stack;
			}
		}
		setModified(true);
		return stack;
	}

	@Override
	public Container clone() {
		try {
			return (Container) super.clone();
		} catch (CloneNotSupportedException e) {
			Logger.getLogger(Container.class).warning("Exception occured", e);
		}
		return null;

	}

	@Override
	public void closeContainer() {
		isOpen = false;
	}

	public abstract String getName();

	@Override
	public ItemStack getItem(int slot) {
		try {
			return slots[slot];
		} catch (Exception e) {
			return null;
		}
	}

	public int getSize() {
		return slots.length;
	}

	public int indexOf(Item object, int damage) {
		return indexOf(new ItemStack(object.id, damage));
	}

	@Override
	public int indexOf(ItemStack object) {
		for (int dofor = 0; dofor < slots.length; dofor++) {
			if (slots[dofor].equals(object)) { return dofor; }
		}
		return -1;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public Iterator<ItemStack> iterator() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (int dofor = 0; dofor < slots.length; dofor++) {
			list.add(slots[dofor]);
		}
		return list.iterator();
	}

	@Override
	public void openContainer() {
		isOpen = true;
	}

	protected void readExtraData(World world, CompoundTag tag) {

	}

	public void setAllItems(ItemStack... items) {
		if (items.length == slots.length) {
			slots = items;
		}
		setModified(true);
	}

	@Override
	public void setItem(int slot, ItemStack item) {
		if (item != null) {
			slots[slot] = item.clone();
		} else {
			slots[slot] = null;
		}
		setModified(true);
	}

	@Override
	public void setItems(int beginIndex, int endIndex, ItemStack stack) {
		for (int dofor = beginIndex; dofor < endIndex; dofor++) {
			slots[dofor] = stack.clone();
		}
		setModified(true);
	}

	@Override
	public void setItems(int beginIndex, ItemStack... stacks) {
		for (int dofor = 0; dofor < stacks.length; dofor++) {
			if (beginIndex + dofor >= getSize()) return;
			slots[beginIndex + dofor] = stacks[dofor];
		}
		setModified(true);
	}

	@Override
	public void toggleOpen() {
		isOpen = !isOpen;
	}

	@Override
	public String toString() {
		return "game.voxelcaverns.data.containers.Container: " + getGuiName();
	}

	protected void writeExtraData(World world, CompoundTag tag) {

	}

	public void writeContainer(World world, CompoundTag tag) {
		tag.addTag(new ShortTag("id", world.getRegisteredContainer(getName())));
		tag.setInt("size", getSize());
		ListTag lis = new ListTag("items", CompoundTag.class);
		for (int d = 0; d < getSize(); ++d) {
			if (getItem(d) == null || !getItem(d).exists()) continue;
			CompoundTag i = new CompoundTag("item");
			ItemStack.write(world, getItem(d), i);
			i.setInt("slot", d);
			lis.addTag(i);
		}
		tag.addTag(lis);
		tag.setBoolean("open", isOpen());
		writeExtraData(world, tag);
	}

	public static void initClass() {
	}

	public static Container readContainer(World world, CompoundTag tag) {
		try {
			short id = tag.getShortTag("id").getValue();
			Constructor<? extends Container> ccons = types.get(world.getContainerName(id));
			Container c = ccons.newInstance();
			c.slots = new ItemStack[tag.getInt("size")];
			ListTag lis = tag.getListTag("items");
			while (lis.hasNext()) {
				CompoundTag t = (CompoundTag) lis.getNextTag();
				ItemStack i = ItemStack.read(world, t);
				c.slots[t.getInt("slot")] = i;
			}
			c.isOpen = tag.getBoolean("open");
			c.readExtraData(world, tag);
			return c;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ItemStack leftClick(int slot, ItemStack heldItem) {
		if (getItem(slot) == null) {
			if (heldItem != null) {
				setItem(slot, heldItem.clone());
				heldItem = null;
			}
		} else {
			// if(SingleplayerUtils.getKeyboard().isKeyDown(Keys.CONTROL)){
			// setItem(slot, containerGui.controlClick(getItem(slot), slot));
			// return heldItem;
			// }
			if (heldItem != null) {
				heldItem = getItem(slot).combineItemStack(heldItem);
			} else {
				heldItem = getItem(slot).clone();
				setItem(slot, null);

			}
		}
		setModified(true);
		return heldItem;
	}

	public ItemStack rightClick(int slot, ItemStack heldItem) {
		if (getItem(slot) == null) {
			if (heldItem != null) {
				setItem(slot, heldItem.clone());
				getItem(slot).setAmount(1);
				heldItem.decrementAmount();
			}
		} else {
			if (heldItem != null) {
				if (heldItem.equals(getItem(slot))) {
					if (getItem(slot).incrementAmount()) {
						heldItem.decrementAmount();
					}
				}
			} else {
				int total = getItem(slot).getAmount();
				int a = total / 2;
				int b = total - a;
				heldItem = getItem(slot).clone();
				heldItem.setAmount(b);
				getItem(slot).setAmount(a);

			}
		}
		setModified(true);
		return heldItem;
	}

	// /**
	// * @param containerGui the containerGui to set
	// */
	// public static void setContainerGui(IContainerGui containerGui) {
	// Container.containerGui = containerGui;
	// }

}
