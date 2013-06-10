package vc4.vanilla.generation.village.style;

import vc4.vanilla.generation.village.WeightedBuilding;
import vc4.vanilla.generation.village.building.BuildingHouse;
import vc4.vanilla.generation.village.building.BuildingMine;

public class VillageStyleBasic extends VillageStyle {

	
	public VillageStyleBasic() {
		addBuilding(new WeightedBuilding(new BuildingHouse(), 100));
		addBuilding(new WeightedBuilding(new BuildingMine(), 10));
	}
}
