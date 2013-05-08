package vc4.vanilla.util;

import vc4.api.item.ItemStack;

public class WoodBlocks {

	private static int maxData = 7;
	private static String[] names = new String[]{
		"oak", "birch", "willow", "ash", "chestnut", "redwood", "kapok"
	};
	
	public static ItemStack[] genCreativeItems(int bid){
		ItemStack[] result = new ItemStack[maxData];
		for(int d = 0; d < maxData; ++d){
			result[d] = new ItemStack(bid, d);
		}
		return result;
	}
	
	public static String getName(int id){
		return names[id];
	}
}
