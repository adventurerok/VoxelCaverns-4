package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.generation.dungeon.*;

public class DungeonRoomStairs extends DungeonRoom {

	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		Direction stairDir = Direction.getDirection(4 + dungeon.getRand().nextInt(2));
		int stairHeight = 4 + dungeon.getRand().nextInt(3) * 6;
		
		ArrayList<Door> result = new ArrayList<>();
		
		Vector3l start = door.left;
		start = start.move(2, door.dir.counterClockwise());
		if (stairDir == Direction.DOWN) start = start.move(stairHeight - 1, stairDir);
		if (!dungeon.inBounds(start)) return null;
		
		Vector3l end = door.right;
		end = end.move(2, door.dir.clockwise());
		end = end.move(stairHeight + 1, door.dir);
		if (stairDir == Direction.UP) end = end.move(stairHeight - 1, stairDir);
		if (!dungeon.inBounds(end)) return null;
		
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		
		
		result.add(door.flip());
		result.add(door.move(stairHeight + 1, door.dir).move(stairHeight - 1, stairDir));
		
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, end.y + 3, ez - 1);
		RoomInfo info = new RoomInfo(bb, result);
		if (!dungeon.addRoom(info)) return null;
		
		// System.out.println("X size: " + (ex - sx) + ", Z size: " + (ez - sz));
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				long by = door.left.y - 1;
				if (door.dir.isNorthSouth() && x != start.x && x != start.move(1, door.dir).x && x != end.x) {
					if (stairDir == Direction.DOWN) by -= Math.abs(start.move(1, door.dir).x - x);
					else by += Math.abs(start.move(1, door.dir).x - x);
				} else if (door.dir.isNorthSouth() && x == end.x) {
					if (stairDir == Direction.DOWN) by -= stairHeight - 1;
					else by += stairHeight - 1;
				} else if (door.dir.isEastWest() && z != start.z && z != start.move(1, door.dir).z && x != end.z) {
					if (stairDir == Direction.DOWN) by -= Math.abs(start.move(1, door.dir).z - z);
					else by += Math.abs(start.move(1, door.dir).z - z);
				} else if (door.dir.isEastWest() && z == end.z) {
					if (stairDir == Direction.DOWN) by -= stairHeight - 1;
					else by += stairHeight - 1;
				}
				if (by > end.y) by = end.y;
				for (long y = by; y < end.y + 5; ++y) {
					if (y == by - 1 || y == end.y + 4) {
						dungeon.setDungeonBlock(x, y, z);
					} else if(y == by){
						if (x == sx || x == ex || z == sz || z == ez) dungeon.setDungeonBlock(x, y, z);
						else{
							if(y == start.y - 1) dungeon.setDungeonBlock(x, y, z);
							else if(stairDir == Direction.DOWN) dungeon.setStairBlock(x, y, z, door.dir.id());
							else dungeon.setStairBlock(x, y, z, door.dir.opposite().id());
						}
					} else if (x == sx || x == ex || z == sz || z == ez) {
//						if ((((x == sx + 2 || x == sx + 3) && xSize == 5) || ((z == sz + 2 || z == sz + 3) && zSize == 5)) && y < by + 3 && y > by) {
//							dungeon.setEmptyBlock(x, y, z);
//							continue;
//						}
						dungeon.setDungeonBlock(x, y, z);
						
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		//dungeon.clearDoors(info);
		return result;
	}

}
