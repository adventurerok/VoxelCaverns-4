package vc4.vanilla.generation.village.style;

import vc4.vanilla.generation.village.WeightedBuilding;
import vc4.vanilla.generation.village.wood.*;

public class VillageStyleWood extends VillageStyle {

	
	public VillageStyleWood() {
		addBuilding(new WeightedBuilding(new WoodHouse(), 100));
		addBuilding(new WeightedBuilding(new WoodMine(), 10));
		addBuilding(new WeightedBuilding(new WoodTower(), 10));
	}
}
