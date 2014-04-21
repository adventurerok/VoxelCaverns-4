package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.*;

public class DungeonRoomCacti extends DungeonRoom {

	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		result.add(door.clone().flip());
		result.add(door.move(9, door.dir));
		result.add(Door.genDoor(door.left.move(4, door.dir.counterClockwise()).move(4, door.dir), door.dir));
		result.add(Door.genDoor(door.right.move(4, door.dir.clockwise()).move(4, door.dir), door.dir));
		
		Vector3l start = door.left;
		start = start.move(4, door.dir.counterClockwise());
		if (!dungeon.inBounds(start)) return null;
		
		Vector3l end = door.right;
		end = end.move(4, door.dir.clockwise());
		end = end.move(9, door.dir);
		if (!dungeon.inBounds(end)) return null;
		
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		
		RoomBB bb = new RoomBB(sx + 1, start.y - 1, sz + 1, ex - 1, start.y + 3, ez - 1);
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
		
		world.setBlockId(sx + 3, start.y - 1, sz + 3, Vanilla.sand.uid);
		world.setBlockId(sx + 6, start.y - 1, sz + 3, Vanilla.sand.uid);
		world.setBlockId(sx + 6, start.y - 1, sz + 6, Vanilla.sand.uid);
		world.setBlockId(sx + 3, start.y - 1, sz + 6, Vanilla.sand.uid);
		
		world.setBlockId(sx + 3, start.y, sz + 3, Vanilla.cactus.uid);
		world.setBlockId(sx + 6, start.y, sz + 3, Vanilla.cactus.uid);
		world.setBlockId(sx + 6, start.y, sz + 6, Vanilla.cactus.uid);
		world.setBlockId(sx + 3, start.y, sz + 6, Vanilla.cactus.uid);
		
		if(dungeon.getRand().nextDouble() < 0.5) world.setBlockId(sx + 3, start.y + 1, sz + 3, Vanilla.cactus.uid);
		if(dungeon.getRand().nextDouble() < 0.5) world.setBlockId(sx + 6, start.y + 1, sz + 3, Vanilla.cactus.uid);
		if(dungeon.getRand().nextDouble() < 0.5) world.setBlockId(sx + 6, start.y + 1, sz + 6, Vanilla.cactus.uid);
		if(dungeon.getRand().nextDouble() < 0.5) world.setBlockId(sx + 3, start.y + 1, sz + 6, Vanilla.cactus.uid);
		
		return result;
	}

}
