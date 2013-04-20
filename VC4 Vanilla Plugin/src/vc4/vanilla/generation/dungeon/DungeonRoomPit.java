package vc4.vanilla.generation.dungeon;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;

public class DungeonRoomPit extends DungeonRoom {

	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		if(dungeon.rand.nextBoolean()) door.move(14, Direction.UP);
		Vector3l start = door.left;
		start = start.move(2, door.dir.counterClockwise());
		if(!dungeon.inBounds(start)) return result;
		Vector3l end = door.right;
		end = end.move(2, door.dir.clockwise());
		end = end.move(5, door.dir);
		if(!dungeon.inBounds(end)) return result;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		RoomBB bb = new RoomBB(sx + 1, start.y - 14, sz + 1, ex - 1, start.y + 3, ez - 1);
		if(!dungeon.addRoom(bb)) return result;
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 15; y < start.y + 5; ++y){
					if(y == start.y - 15 || y == start.y + 4){
						dungeon.setDungeonBlock(x, y, z);
					} else if(x == sx || x == ex || z == sz || z == ez){
						if((x == sx + 2 || x == sx + 3 || z == sz + 2 || z == sz + 3 ) && ((y < start.y + 2 && y > start.y - 1) || y < start.y - 12 && y > start.y - 15)){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						}
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		result.add(door.clone().setNewRoomDir(door.dir.opposite()));
		result.add(door.move(5, door.dir));
		result.add(Door.genDoor(door.left.move(2, door.dir.counterClockwise()).move(3, door.dir), door.dir).setNewRoomDir(door.dir.counterClockwise()));
		result.add(Door.genDoor(door.right.move(2, door.dir.clockwise()).move(3, door.dir), door.dir).setNewRoomDir(door.dir.clockwise()));
		result.add(door.clone().setNewRoomDir(door.dir.opposite()).move(14, Direction.DOWN));
		result.add(door.move(5, door.dir).move(14, Direction.DOWN));
		result.add(Door.genDoor(door.left.move(2, door.dir.counterClockwise()).move(3, door.dir), door.dir).setNewRoomDir(door.dir.counterClockwise()).move(14, Direction.DOWN));
		result.add(Door.genDoor(door.right.move(2, door.dir.clockwise()).move(3, door.dir), door.dir).setNewRoomDir(door.dir.clockwise()).move(14, Direction.DOWN));
		return result;
	}

	@Override
	public int getWeight() {
		return 5;
	}

}
