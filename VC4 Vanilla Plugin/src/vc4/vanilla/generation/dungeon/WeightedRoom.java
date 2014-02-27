package vc4.vanilla.generation.dungeon;

import vc4.vanilla.generation.dungeon.room.DungeonRoom;

public class WeightedRoom {

	DungeonRoom room;
	int weight;

	public WeightedRoom(DungeonRoom room, int weight) {
		super();
		this.room = room;
		this.weight = weight;
	}

	public DungeonRoom getRoom() {
		return room;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
