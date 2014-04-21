package vc4.vanilla.generation.dungeon.style;

import vc4.vanilla.generation.dungeon.WeightedRoom;
import vc4.vanilla.generation.dungeon.room.*;

public class DungeonStyleLibrary extends DungeonStyle {

	public DungeonStyleLibrary() {
		addRoom(new WeightedRoom(new DungeonRoomLibrary(), 15));
		addRoom(new WeightedRoom(new DungeonRoomClimb(), 2));
		//addRoom(new WeightedRoom(new DungeonRoomLoot(), 1));
		setRoomFailChance(0.25);
	}

	@Override
	public int getWeight() {
		return 10;
	}
}
