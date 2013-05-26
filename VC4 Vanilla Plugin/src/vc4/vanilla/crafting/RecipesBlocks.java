package vc4.vanilla.crafting;

import java.util.*;

import vc4.api.crafting.*;
import vc4.vanilla.Vanilla;

public class RecipesBlocks implements RecipeList {

	@Override
	public Collection<CraftingRecipe> getRecipes() {
		List<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();

		// wood - planks
		recipes.add(new CraftingRecipe(new CraftingStack[] { new CraftingStack(Vanilla.logV.uid, 0, 1).setAnyData(true).setOutputData(true) }, new CraftingStack(Vanilla.planks.uid, 0, 4)));

		// planks - crafting table
		recipes.add(new CraftingRecipe(new CraftingStack[] { new CraftingStack(Vanilla.planks.uid, 0, 4).setAnyData(true) }, new CraftingStack(Vanilla.workbench.uid, 0, 1)));

		// Half planks
		recipes.add(new CraftingRecipe(new CraftingStack[] { new CraftingStack(Vanilla.planks.uid, 0, 1).setAnyData(true).setOutputData(true) }, new CraftingStack(Vanilla.planksHalf.uid, 0, 2)));
		recipes.add(new CraftingRecipe(new CraftingStack[] { new CraftingStack(Vanilla.planksHalf.uid, 0, 2).setAnyData(true).setOutputData(true) }, new CraftingStack(Vanilla.planks.uid, 0, 1)));

		// Half bricks
		recipes.add(new CraftingRecipe(new CraftingStack[] { new CraftingStack(Vanilla.brick.uid, 0, 1).setAnyData(true).setOutputData(true) }, new CraftingStack(Vanilla.brickHalf.uid, 0, 2)));
		recipes.add(new CraftingRecipe(new CraftingStack[] { new CraftingStack(Vanilla.brickHalf.uid, 0, 2).setAnyData(true).setOutputData(true) }, new CraftingStack(Vanilla.brick.uid, 0, 1)));

		return recipes;
	}

}
