package vc4.api.crafting;

import java.util.Arrays;

import vc4.api.item.ItemStack;

/*
 * Represents a crafting recipie in the game
 * @author adventurerok
 */
public class CraftingRecipe {

	int inputData = -1;
	protected CraftingStack[] itemInput;
	protected ItemStack[] itemOutput;
	protected short[] itemsRequired = new short[0];
	/*
	 * Creates a new crafting recipie, with the input and output specified
	 */
	public CraftingRecipe(CraftingStack[] input, ItemStack... output){
		Arrays.sort(input);
		itemInput = input;
		itemOutput = output;
	}
	public CraftingRecipe setTools(short[] tools){
		Arrays.sort(itemsRequired = tools);
		return this;
	}
	
	public int getSortingInt(){
		return itemInput != null && itemInput.length > 0 ? itemInput[0].getId() : 0;
	}
	
	/*
	 * Checks if the recipie can be crafted
	 * Please use Arrays.sort(input) before using this or else it won't work
	 * @param input The sorted input to check against the recipies information
	 * 
	 */
	public boolean canCraftNow(ItemStack[] input, short[] item){
		inputData = -1;
		if(input.length != itemInput.length){
			return false;
		}
		if(!hasItems(item)) return false;
		for(int dofor = 0; dofor < input.length; dofor++){
			if(!itemInput[dofor].equals(input[dofor]) || input[dofor].getAmount() != itemInput[dofor].getAmount()) return false;
			if(itemInput[dofor].isOutputData()) inputData = input[dofor].getDamage();
		}
		return true;
	}
	
	public boolean hasItems(short[] items){
		for(int d = 0; d < itemsRequired.length; ++d){
			short s = itemsRequired[d];
			for(int i = 0; i < items.length; ++d){
				if(items[i] == s) break;
				if(i == items.length - 1) return false;
			}
		}
		return true;
	}
	
	public int getAmountOfResults(){
		return itemOutput.length;
	}
	public ItemStack[] getInputRequired(){
		return itemInput;
	}
	public ItemStack[] getPossibleOutputs(ItemStack...stacks){
		if(inputData == -1)return itemOutput;
		ItemStack[] out = new ItemStack[itemOutput.length];
		for(int d = 0; d < out.length; ++d){
			out[d] = itemOutput[d].clone().setDamage(inputData);
		}
		return out;
	}
	public ItemStack getResult(int number){
		return itemOutput[number];
	}
	public boolean hasMoreThanOneResult(){
		return itemOutput.length > 1;
	}
	
	
}
