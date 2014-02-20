package vc4.api.crafting;

import java.util.*;

import vc4.api.Resources;
import vc4.api.VoxelCaverns;
import vc4.api.item.ItemStack;

public class CraftingManager {

	static HashMap<Integer, List<CraftingRecipe>> sorted = new HashMap<>();
	static HashMap<Integer, Integer> toolIcons = new HashMap<>();
	
	private static Integer zero = Integer.valueOf(0);
	
	public static void setToolIcon(int tool, String icon){
		if(!VoxelCaverns.hasGraphics()) return;
		int ico = Resources.getAnimatedTexture("crafting").getArrayIndex(icon);
		toolIcons.put(tool, ico);
	}
	
	public static int getToolIcon(int tool){
		return toolIcons.get(tool);
	}
	
	public static void emptyRecipes(){
		sorted.clear();
		toolIcons.clear();
	}
	
	public static void addRecipes(Collection<CraftingRecipe> recipes) {
		for(CraftingRecipe c : recipes){
			int s = c.getSortingInt(); //input[0] or null
			List<CraftingRecipe> l = sorted.get(s);
			if(l == null){
				l = new ArrayList<>();
				sorted.put(s, l);
			}
			l.add(c);
		}
	}
	
	public static void addRecipes(RecipeList recipes){
		addRecipes(recipes.getRecipes());
	}
	
	public static void addCraftingRecipe(CraftingRecipe c){
		int s = c.getSortingInt(); //input[0].getId() or 0
		List<CraftingRecipe> l = sorted.get(s);
		if(l == null){
			l = new ArrayList<>();
			sorted.put(s, l);
		}
		l.add(c);
	}
	public static ItemStack[] tryCraft(short[] item, ItemStack... input){
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		result.addAll(tryCraft(sorted.get(zero), item, input));
		if(input.length > 0) result.addAll(tryCraft(sorted.get(input[0].getId()), item, input));
		if(result.size() < 1)return null;
		else return result.toArray(new ItemStack[result.size()]);
	}
	
	private static Collection<ItemStack> tryCraft(List<CraftingRecipe> recipes, short[] item, ItemStack... input){
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		if(recipes == null) return result;
		for(int dofor = 0; dofor < recipes.size(); dofor++){
			if(recipes.get(dofor).canCraftNow(input, item)){
				ItemStack[] out = recipes.get(dofor).getPossibleOutputs(input);
				result.ensureCapacity(result.size() + out.length);
				for(int pos = 0; pos < out.length; ++pos){
					result.add(out[pos]);
				}
			}
		}
		return result;
	}
	
	
}
