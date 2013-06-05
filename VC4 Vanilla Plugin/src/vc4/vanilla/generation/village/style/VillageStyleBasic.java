package vc4.vanilla.generation.village.style;

import vc4.vanilla.generation.village.WeightedBuilding;
import vc4.vanilla.generation.village.building.BuildingHouse;

public class VillageStyleBasic extends VillageStyle {

	
	public VillageStyleBasic() {
		addRoom(new WeightedBuilding(new BuildingHouse(), 15));
	}
}
