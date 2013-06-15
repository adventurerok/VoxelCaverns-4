package vc4.vanilla.generation.village;


public class WeightedBuilding {

	Building building;
	int weight;
	
	
	
	public WeightedBuilding(Building room, int weight) {
		super();
		this.building = room;
		this.weight = weight;
	}
	public Building getBuilding() {
		return building;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	
}
