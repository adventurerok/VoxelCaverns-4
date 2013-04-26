package vc4.vanilla.generation.dungeon.style;

import java.util.ArrayList;

import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.WeightedRoom;

public class DungeonStyle {

	protected int brickId, mossId, type;
	private ArrayList<WeightedRoom> rooms = new ArrayList<>();
	private double roomFailChance = 0.4;
	
	public void setRoomFailChance(double roomFailChance) {
		this.roomFailChance = roomFailChance;
	}
	
	public double getRoomFailChance() {
		return roomFailChance;
	}
	
	public boolean canGenerate(World world, long x, long y, long z){
		return y < -32;
	}
	
	public int getBrickId() {
		return brickId;
	}

	public int getMossId() {
		return mossId;
	}

	public int getType() {
		return type;
	}
	
	public void addRoom(WeightedRoom room){
		rooms.add(room);
	}
	
	public ArrayList<WeightedRoom> getRooms() {
		return rooms;
	}

	public DungeonStyle() {
		type = 4;
	}
	
	public void onWorldLoad(World world){
		brickId = Vanilla.brick.uid;
		mossId = Vanilla.mossBrick.uid;
	}
	
	public int getWeight(){
		return 100;
	}
}
