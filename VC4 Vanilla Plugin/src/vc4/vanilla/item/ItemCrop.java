package vc4.vanilla.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.item.ItemMultitexture;
import vc4.api.item.ItemStack;
import vc4.api.util.Colors;
import vc4.vanilla.ItemTexture;

public class ItemCrop extends ItemMultitexture {
	
	static Color[] colors = new Color[4];
	
	static{
		colors[0] = new Color(210, 200, 0);
		colors[1] = new Color(210, 60, 0);
	}

	public ItemCrop(int id) {
		super(id);
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		res.add(new ItemStack(id, 0, 1));
		res.add(new ItemStack(id, 1, 1));
		return res;
	}
	
	@Override
	public String getModifiedItemName(ItemStack stack) {
		switch(stack.getDamage()){
			case 0: return "wheat";
			case 1: return "barley";
			default: return "crop";
		}
	}
	
	@Override
	public Color getColor(ItemStack item) {
		return colors[item.getDamage()];
	}
	
	@Override
	public Color getMultitextureColor(ItemStack item) {
		return Colors.brown;
	}
	
	@Override
	public int getTextureIndex(ItemStack current) {
		return ItemTexture.harvestBack;
	}
	
	@Override
	public int getMultitextureTextureIndex(ItemStack item) {
		return ItemTexture.harvestFront;
	}

}
