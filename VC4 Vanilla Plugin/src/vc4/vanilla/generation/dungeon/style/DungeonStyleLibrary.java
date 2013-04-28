package vc4.vanilla.generation.dungeon.style;

import vc4.vanilla.generation.dungeon.WeightedRoom;
import vc4.vanilla.generation.dungeon.room.DungeonRoomLibrary;

public class DungeonStyleLibrary extends DungeonStyle {

	
	public DungeonStyleLibrary() {
		addRoom(new WeightedRoom(new DungeonRoomLibrary(), 2));
		setRoomFailChance(0.45);
	}
	
	@Override
	public int getWeight() {
		return 10;
	}
}
