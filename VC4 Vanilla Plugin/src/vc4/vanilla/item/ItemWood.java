package vc4.vanilla.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

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
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> result = new ArrayList<>();
		for(int d = 0; d < BlockPlanks.frontColors.length; ++d){
			if(BlockPlanks.frontColors[d] != null) result.add(new ItemStack(id, d, 1));
		}
		return result;
	}

	@Override
	public Color getColor(ItemStack item) {
		return BlockPlanks.frontColors[item.getDamage()];
	}

}
