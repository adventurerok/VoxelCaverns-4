package vc4.vanilla.item;

import java.awt.Color;

import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.vanilla.block.BlockPlanks;

public class ItemWood extends Item{

	public ItemWood(int id, int textureIndex) {
		super(id, textureIndex);
		// TASK Auto-generated constructor stub
	}

	public ItemWood(int id) {
		super(id);
		// TASK Auto-generated constructor stub
	}

	@Override
	public Color getColor(ItemStack item) {
		return BlockPlanks.frontColors[item.getDamage()];
	}

}
