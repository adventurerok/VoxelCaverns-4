package vc4.vanilla.util;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.item.ItemStack;

public class WoodBlocks {

	private static int maxData = 8;
	private static String[] names = new String[]{
		"oak", "birch", "willow", "ash", "chestnut", "redwood", "kapok", "cypress"
	};
	
	public static byte getNameId(String name){
		for(int d = 0; d < names.length; ++d){
			if(names[d].equals(name)) return (byte) d;
		}
		return -1;
	}
	
	public static Collection<ItemStack> genCreativeItems(int bid){
		ArrayList<ItemStack> result = new ArrayList<>();
		for(int d = 0; d < maxData; ++d){
			result.add(new ItemStack(bid, d));
		}
		return result;
	}
	
	public static String getName(int id){
		return names[id];
	}
}
