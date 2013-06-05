package vc4.vanilla.generation.village.style;

import java.util.ArrayList;

import vc4.api.world.World;
import vc4.vanilla.generation.village.WeightedBuilding;

public class VillageStyle {

	private ArrayList<WeightedBuilding> rooms = new ArrayList<>();
	private int maxRooms = 75;
	
	
	
	
	public boolean canGenerate(World world, long x, long y, long z){
		return y < -32;
	}
	
	
	public int getMaxRooms() {
		return maxRooms;
	}
	
	public void setMaxRooms(int maxRooms) {
		this.maxRooms = maxRooms;
	}

	
	public void addRoom(WeightedBuilding room){
		rooms.add(room);
	}
	
	public ArrayList<WeightedBuilding> getBuildings() {
		return rooms;
	}

	
	public void onWorldLoad(World world){
		
	}
	
	public int getWeight(){
		return 100;
	}
}
