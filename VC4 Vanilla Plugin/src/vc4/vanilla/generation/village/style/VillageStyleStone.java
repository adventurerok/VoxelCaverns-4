package vc4.vanilla.generation.village.style;

import vc4.vanilla.generation.village.WeightedBuilding;
import vc4.vanilla.generation.village.stone.*;

public class VillageStyleStone extends VillageStyle {

	
	public VillageStyleStone() {
		addBuilding(new WeightedBuilding(new StoneHouse(), 100));
		addBuilding(new WeightedBuilding(new StoneMine(), 10));
		addBuilding(new WeightedBuilding(new StoneTower(), 10));
		wall = new StoneWall();
	}
	
	@Override
	public int getWeight() {
		return 60;
	}
}
