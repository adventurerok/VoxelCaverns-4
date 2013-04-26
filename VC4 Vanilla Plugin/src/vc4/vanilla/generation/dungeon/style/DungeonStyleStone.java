package vc4.vanilla.generation.dungeon.style;

import vc4.vanilla.generation.dungeon.WeightedRoom;
import vc4.vanilla.generation.dungeon.room.DungeonRoomBase;
import vc4.vanilla.generation.dungeon.room.DungeonRoomCorridor;
import vc4.vanilla.generation.dungeon.room.DungeonRoomLeftTurn;
import vc4.vanilla.generation.dungeon.room.DungeonRoomPit;
import vc4.vanilla.generation.dungeon.room.DungeonRoomRightTurn;
import vc4.vanilla.generation.dungeon.room.DungeonRoomStairs;

public class DungeonStyleStone extends DungeonStyle{

	public DungeonStyleStone() {
		addRoom(new WeightedRoom(new DungeonRoomBase(), 15));
		addRoom(new WeightedRoom(new DungeonRoomCorridor(), 20));
		addRoom(new WeightedRoom(new DungeonRoomLeftTurn(), 6));
		addRoom(new WeightedRoom(new DungeonRoomRightTurn(), 6));
		addRoom(new WeightedRoom(new DungeonRoomStairs(), 8));
		addRoom(new WeightedRoom(new DungeonRoomPit(), 5));
	}
}
