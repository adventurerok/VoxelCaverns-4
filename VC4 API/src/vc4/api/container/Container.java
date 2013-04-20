package vc4.api.container;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.jnbt.*;

import vc4.api.item.Item;
import vc4.api.item.ItemStack;

public abstract class Container implements IContainer, Iterable<ItemStack>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1914854850244654107L;
	//private static IContainerGui containerGui;
	protected boolean isOpen = false;
	protected ItemStack[] slots;

	public static HashMap<Short, Class<? extends Container>> types = new HashMap<Short, Class<? extends Container>>();

	protected String name = "Container";
	static {
//		registerContainer(1, ContainerInventory.class);
//		registerContainer(2, ContainerCreative.class);
//		registerContainer(3, ContainerArmour.class);
//		registerContainer(4, ContainerChest.class);
//		registerContainer(5, ContainerFurnace.class);
	}

	public static void registerContainer(int id, Class<? extends Container> c) {
		types.put((short) id, c);
	}

	protected Container() {
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
	}

	@Override
	public ItemStack addItemStack(ItemStack stack) {
		if (stack == null || !stack.checkIsNotEmpty())
			return null;
		stack = stack.clone();
		for (int dofor = 0; dofor < slots.length; dofor++) {
			if (stack.equals(slots[dofor])) {
				stack = slots[dofor].combineItemStack(stack);
				if (stack == null || !stack.checkIsNotEmpty()) {
					stack = null;
					return stack;
				}
			}
		}
		for (int dofor = 0; dofor < slots.length; dofor++) {
			if (slots[dofor] == null || !slots[dofor].checkIsNotEmpty()) {
				slots[dofor] = stack;
				stack = null;
				return stack;
			}
		}
		return stack;
	}
	
	public ItemStack addItemStack(ItemStack stack, int min, int max) {
		if (stack == null || !stack.checkIsNotEmpty())
			return null;
		stack = stack.clone();
		for (int dofor = min; dofor < max; dofor++) {
			if (stack.equals(slots[dofor])) {
				stack = slots[dofor].combineItemStack(stack);
				if (stack == null || !stack.checkIsNotEmpty()) {
					stack = null;
					return stack;
				}
			}
		}
		for (int dofor = min; dofor < max; dofor++) {
			if (slots[dofor] == null || !slots[dofor].checkIsNotEmpty()) {
				slots[dofor] = stack;
				stack = null;
				return stack;
			}
		}
		return stack;
	}

	@Override
	public Container clone() {
		Container c = getInstance(getId());
		c.slots = slots;
		c.name = name;
		return c;

	}

	@Override
	public void closeContainer() {
		isOpen = false;
	}

	/**
	 * Gets the entity id as a 12-bit short (0-4095, inclusive)
	 * 
	 * @return The id of the container
	 */
	public abstract short getId();

	// public ItemStack[] toArray(){
	// return slots;
	// }
	private Container getInstance(final short id) {
		return new Container(slots.length) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2024203236389294409L;

			@Override
			public short getId() {
				return id;
			}
		};
	}

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
			if (slots[dofor].equals(object)) {
				return dofor;
			}
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


	protected void readExtraData(CompoundTag tag) {

	}


	public void setAllItems(ItemStack... items) {
		if (items.length == slots.length) {
			slots = items;
		}
	}

	@Override
	public void setItem(int slot, ItemStack item) {
		if (item != null) {
			slots[slot] = item.clone();
		} else {
			slots[slot] = null;
		}
	}

	@Override
	public void setItems(int beginIndex, int endIndex, ItemStack stack) {
		for (int dofor = beginIndex; dofor < endIndex; dofor++) {
			slots[dofor] = stack.clone();
		}
	}

	@Override
	public void setItems(int beginIndex, ItemStack... stacks) {
		for (int dofor = 0; dofor < stacks.length; dofor++) {
			if (beginIndex + dofor >= getSize())
				return;
			slots[beginIndex + dofor] = stacks[dofor];
		}
	}

	@Override
	public void toggleOpen() {
		isOpen = !isOpen;
	}

	@Override
	public String toString() {
		return "game.voxelcaverns.data.containers.Container: " + name;
	}


	protected void writeExtraData(CompoundTag tag) {

	}


	public void writeTo(CompoundTag tag) {
		tag.addTag(new EByteTag("id", getId()));
		tag.setInt("size", getSize());
		ListTag lis = new ListTag("items", CompoundTag.class);
		for(int d = 0; d < getSize(); ++d){
			if(getItem(d) == null || !getItem(d).checkIsNotEmpty()) continue;
			CompoundTag i = new CompoundTag("item");
			ItemStack.write(getItem(d), i);
			i.setInt("slot", d);
			lis.addTag(i);
		}
		tag.setBoolean("open", isOpen());
		writeExtraData(tag);
	}

	public static void initClass() {
	}

	public static Container readFrom(CompoundTag tag) {
		try {
			short id = tag.getEByteTag("id").getValue();
			Class<? extends Container> ccs = types.get(id);
			Constructor<? extends Container> ccons = ccs.getConstructor();
			Container c = ccons.newInstance();
			c.slots = new ItemStack[tag.getInt("size")];
			ListTag lis = tag.getListTag("items");
			while(lis.hasNext()){
				CompoundTag t = (CompoundTag) lis.getNextTag();
				ItemStack i = ItemStack.read(t);
				c.slots[t.getInt("slot")] = i;
			}
			c.isOpen = tag.getBoolean("open");
			c.readExtraData(tag);
			return c;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
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
	
	public ItemStack leftClick(int slot, ItemStack heldItem){
		if (getItem(slot) == null) {
			if (heldItem != null) {
				setItem(slot, heldItem.clone());
				heldItem = null;
			}
		} else {
//			if(SingleplayerUtils.getKeyboard().isKeyDown(Keys.CONTROL)){
//				setItem(slot, containerGui.controlClick(getItem(slot), slot));
//				return heldItem;
//			}
			if (heldItem != null) {
				heldItem = getItem(slot).combineItemStack(heldItem);
			} else {
				heldItem = getItem(slot).clone();
				setItem(slot, null);

			}
		}
		return heldItem;
	}
	
	public ItemStack rightClick(int slot, ItemStack heldItem){
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
		
		return heldItem;
	}
	
//	/**
//	 * @param containerGui the containerGui to set
//	 */
//	public static void setContainerGui(IContainerGui containerGui) {
//		Container.containerGui = containerGui;
//	}

}
