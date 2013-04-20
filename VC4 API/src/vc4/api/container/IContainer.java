package vc4.api.container;

import java.util.Iterator;

import vc4.api.item.ItemStack;

public interface IContainer {

	public IContainer clone();
	public void closeContainer();
	public void openContainer();
	public void toggleOpen();
	public ItemStack getItem(int slot);
	public int indexOf(ItemStack stack);
	public boolean isOpen();
	public Iterator<ItemStack> iterator();
	public ItemStack addItemStack(ItemStack stack);
	public void setItem(int slot, ItemStack item);
	public void setItems(int beginIndex, int endIndex, ItemStack stack);
	public void setItems(int beginIndex, ItemStack... stacks);
	
}
