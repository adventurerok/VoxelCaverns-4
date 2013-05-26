package vc4.vanilla.item;

import vc4.api.item.ItemFood;
import vc4.vanilla.ItemTexture;

public class ItemVanillaFood extends ItemFood {

	public ItemVanillaFood(int id) {
		super(id);
		setFoodData(0, ItemTexture.cheese, "cheese", 10, 15, 101);
		setFoodData(1, ItemTexture.bread, "bread", 15, 25, 105);
	}

}
