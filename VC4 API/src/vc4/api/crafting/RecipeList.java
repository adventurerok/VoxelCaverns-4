package vc4.api.crafting;

import java.util.Collection;

/**
 * Represents a Collection, or "Book" of recipies
 * 
 * @author Paul Durbaba
 * 
 */
public interface RecipeList {

	/**
	 * Gets the recipies that this RecipeList has
	 * 
	 * @return The Collection of recipes defined by this Class
	 */
	public Collection<CraftingRecipe> getRecipes();
}
