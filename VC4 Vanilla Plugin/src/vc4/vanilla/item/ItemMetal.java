package vc4.vanilla.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.vanilla.block.BlockOre;

public class ItemMetal extends Item {

	String typeName;

	public ItemMetal(int id, int textureIndex, String name) {
		super(id, textureIndex);
		typeName = name;
	}

	@Override
	public String getModifiedItemName(ItemStack stack) {
		return typeName + "." + BlockOre.oreNames[stack.getDamage()];
	}

	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>();
		for (int d = 0; d < 32; ++d) {
			if (BlockOre.oreColors[d] != null) result.add(new ItemStack(id, d, 1));
		}
		return result;
	}

	@Override
	public Color getColor(ItemStack item) {
		return BlockOre.oreColors[item.getDamage()];
	}

}
