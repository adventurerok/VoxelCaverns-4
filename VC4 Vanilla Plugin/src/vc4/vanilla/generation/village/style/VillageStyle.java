package vc4.vanilla.generation.village.style;

import java.util.ArrayList;

import vc4.api.world.World;
import vc4.vanilla.generation.village.Wall;
import vc4.vanilla.generation.village.WeightedBuilding;

public class VillageStyle {

	private ArrayList<WeightedBuilding> rooms = new ArrayList<>();
	protected Wall wall;
	private int maxRooms = 75;

	private int totalWeight;

	public int getTotalWeight() {
		return totalWeight;
	}

	public Wall getWall() {
		return wall;
	}

	public boolean canGenerate(World world, long x, long y, long z) {
		return y > -32 && y < 129;
	}

	public int getMaxRooms() {
		return maxRooms;
	}

	public void setMaxRooms(int maxRooms) {
		this.maxRooms = maxRooms;
	}

	public void addBuilding(WeightedBuilding room) {
		rooms.add(room);
		calcWeight();
	}

	private void calcWeight() {
		int max = 0;
		for (WeightedBuilding d : getBuildings()) {
			max += d.getWeight();
		}
		totalWeight = max;
	}

	public ArrayList<WeightedBuilding> getBuildings() {
		return rooms;
	}

	public void onWorldLoad(World world) {

	}

	public int getWeight() {
		return 100;
	}
}
