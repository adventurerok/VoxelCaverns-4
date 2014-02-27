package vc4.vanilla.generation.dungeon.style;

import vc4.vanilla.generation.dungeon.WeightedRoom;
import vc4.vanilla.generation.dungeon.room.DungeonRoomLibrary;
import vc4.vanilla.generation.dungeon.room.DungeonRoomLoot;

public class DungeonStyleLibrary extends DungeonStyle {

	public DungeonStyleLibrary() {
		addRoom(new WeightedRoom(new DungeonRoomLibrary(), 15));
		addRoom(new WeightedRoom(new DungeonRoomLoot(), 1));
		setRoomFailChance(0.45);
	}

	@Override
	public int getWeight() {
		return 10;
	}
}
