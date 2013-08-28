package vc4.vanilla.item;

import java.awt.Color;

import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.vanilla.block.BlockOre;

public class ItemMetal extends Item{

	public ItemMetal(int id, int textureIndex) {
		super(id, textureIndex);
		// TASK Auto-generated constructor stub
	}

	public ItemMetal(int id) {
		super(id);
		// TASK Auto-generated constructor stub
	}
	
	@Override
	public Color getColor(ItemStack item) {
		return BlockOre.oreColors[item.getDamage()];
	}
	
	

}
