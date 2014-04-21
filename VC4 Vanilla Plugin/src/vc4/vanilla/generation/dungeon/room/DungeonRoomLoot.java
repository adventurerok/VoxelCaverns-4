package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.generation.dungeon.*;

public class DungeonRoomLoot extends DungeonRoom {

	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		
		result.add(door.clone().flip());
		
		Vector3l start = door.left;
		start = start.move(3, door.dir.counterClockwise());
		if (!dungeon.inBounds(start)) return null;
		
		Vector3l end = door.right;
		end = end.move(3, door.dir.clockwise());
		end = end.move(7, door.dir);
		if (!dungeon.inBounds(end)) return null;
		
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, start.y + 3, ez - 1);
		RoomInfo info = new RoomInfo(bb, result);
		if (!dungeon.addRoom(info)) return null;
		
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				for (long y = start.y - 1; y < start.y + 5; ++y) {
					if (y == start.y - 1 || y == start.y + 4) {
						dungeon.setDungeonBlock(x, y, z);
					} else if (x == sx || x == ex || z == sz || z == ez) {
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		
		Vector3l lPos;
		if(dungeon.randomChance(0.33)){
			lPos = door.left.move(6, door.dir, 1, door.dir.counterClockwise());
			dungeon.setChestBlock(lPos, dungeon.getStyle().getLootChest());
		}
		if(dungeon.randomChance(0.33)){
			lPos = door.right.move(6, door.dir, 1, door.dir.clockwise());
			dungeon.setChestBlock(lPos, dungeon.getStyle().getLootChest());
		}
		if(dungeon.randomChance(0.33)){
			lPos = door.left.move(2, door.dir, 2, door.dir.counterClockwise());
			dungeon.setChestBlock(lPos, dungeon.getStyle().getLootChest());
		}
		if(dungeon.randomChance(0.33)){
			lPos = door.left.move(5, door.dir, 2, door.dir.counterClockwise());
			dungeon.setChestBlock(lPos, dungeon.getStyle().getLootChest());
		}
		if(dungeon.randomChance(0.33)){
			lPos = door.left.move(2, door.dir, 2, door.dir.clockwise());
			dungeon.setChestBlock(lPos, dungeon.getStyle().getLootChest());
		}
		if(dungeon.randomChance(0.33)){
			lPos = door.left.move(5, door.dir, 2, door.dir.clockwise());
			dungeon.setChestBlock(lPos, dungeon.getStyle().getLootChest());
		}
		return result;
	}

}
