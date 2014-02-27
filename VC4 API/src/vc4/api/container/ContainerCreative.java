package vc4.api.container;

import java.util.ArrayList;

import vc4.api.block.Block;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.logging.Logger;

public class ContainerCreative extends Container {

	public static ItemStack[] creativeItems = getCreativeItems();
	public ArrayList<ItemStack> searchResults = new ArrayList<ItemStack>();
	public String searchTerm = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = 807934544796532170L;

	public ContainerCreative() {
		super(creativeItems);
	}

	public boolean search(String term) {
		if (term == null) return false;
		term = term.trim().toLowerCase();
		if (searchTerm.equalsIgnoreCase(term)) return false;
		if (term.length() == 0) {
			searchTerm = term;
			searchResults.clear();
			return true;
		} else if (searchTerm.isEmpty() || !term.startsWith(searchTerm)) {
			searchTerm = term;
			searchItems();
			return true;
		} else {
			searchTerm = term;
			searchItems(searchResults);
			return true;
		}
	}

	protected void searchItems(ArrayList<ItemStack> items) {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		for (int dofor = 0; dofor < items.size(); ++dofor) {
			if (items.get(dofor).getUnformattedName().trim().toLowerCase().contains(searchTerm)) result.add(items.get(dofor));
		}
		searchResults = result;
	}

	protected void searchItems() {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		for (int dofor = 0; dofor < creativeItems.length; ++dofor) {
			if (creativeItems[dofor] == null) continue;
			if (creativeItems[dofor].getUnformattedName().trim().toLowerCase().contains(searchTerm)) result.add(creativeItems[dofor]);
		}
		searchResults = result;
	}

	private static ItemStack[] getCreativeItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		for (int dofor = 1; dofor < 2048; ++dofor) {
			if (Block.byId(dofor) == null) {
				continue;
			}
			items.addAll(Block.byId(dofor).getCreativeItems());
		}
		int bls = items.size();
		Logger.getLogger("VC4").fine("Added " + bls + " blocks to the Creative Inventory");
		for (int dofor = 2048; dofor < 65536; ++dofor) {
			if (Item.byId(dofor) == null) {
				continue;
			}
			items.addAll(Item.byId(dofor).getCreativeItems());
		}
		int ils = items.size() - bls;
		// int ench = ils - (Item.enchantment.getEnchantmentsInCreative().length * 15);
		int ench = ils;
		Logger.getLogger("VC4").fine("Added " + ils + " items to the Creative Inventory (" + ench + " without enchantment clones)");
		return items.toArray(new ItemStack[items.size()]);
	}

	// private static ItemStack[] fromVarags(ItemStack...itemStacks){
	// return itemStacks;
	// }

	@Override
	public ItemStack getItem(int slot) {
		if (searchTerm.isEmpty()) {
			try {
				return creativeItems[slot];
			} catch (Exception e) {
				return null;
			}
		} else {
			try {
				return searchResults.get(slot);
			} catch (Exception e) {
				return null;
			}
		}
	}

	@Override
	public int getSize() {
		if (searchTerm.isEmpty()) return creativeItems.length;
		else return searchResults.size();
	}

	@Override
	public String getName() {
		return "creative";
	}

}
