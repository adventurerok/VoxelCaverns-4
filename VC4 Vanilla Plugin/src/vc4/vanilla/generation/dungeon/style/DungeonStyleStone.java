package vc4.vanilla.generation.dungeon.style;

import vc4.vanilla.generation.dungeon.WeightedRoom;
import vc4.vanilla.generation.dungeon.room.*;

public class DungeonStyleStone extends DungeonStyle{

	public DungeonStyleStone() {
		addRoom(new WeightedRoom(new DungeonRoomBase(), 15));
		addRoom(new WeightedRoom(new DungeonRoomCorridor(), 20));
		addRoom(new WeightedRoom(new DungeonRoomLeftTurn(), 6));
		addRoom(new WeightedRoom(new DungeonRoomRightTurn(), 6));
		addRoom(new WeightedRoom(new DungeonRoomStairs(), 8));
		addRoom(new WeightedRoom(new DungeonRoomPit(), 5));
		addRoom(new WeightedRoom(new DungeonRoomLibrary(), 2));
		addRoom(new WeightedRoom(new DungeonRoomLoot(), 6));
	}
	
	
}
