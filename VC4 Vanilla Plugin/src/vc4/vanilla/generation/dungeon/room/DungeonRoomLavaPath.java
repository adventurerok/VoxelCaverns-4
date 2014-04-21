package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.*;

public class DungeonRoomLavaPath extends DungeonRoom {

	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		int move = 7 + dungeon.getRand().nextInt(6);
		result.add(door.flip());
		result.add(door.move(move, door.dir));
		
		Vector3l start = door.left;
		start = start.move(3, door.dir.counterClockwise());
		if (!dungeon.inBounds(start)) return null;
		
		Vector3l end = door.right;
		end = end.move(3, door.dir.clockwise());
		end = end.move(move, door.dir);
		if (!dungeon.inBounds(end)) return null;
		
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		
		RoomBB bb = new RoomBB(sx + 1, start.y - 6, sz + 1, ex - 1, start.y + 3, ez - 1);
		RoomInfo info = new RoomInfo(bb, result);
		if (!dungeon.addRoom(info)) return null;
		
		boolean isX = Direction.getDirection(door.left.subtract(start)).getZ() > 0;
		
		
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				for (long y = start.y - 7; y < start.y + 5; ++y) {
					if (y == start.y - 7 || y == start.y + 4) {
						dungeon.setDungeonBlock(x, y, z);
					} else if (x == sx || x == ex || z == sz || z == ez) {
						dungeon.setDungeonBlock(x, y, z);
					} else if(y == start.y - 1){
						if(isX && (z == sz + 3 || z == sz + 4)){
							if(dungeon.getRand().nextDouble() < 0.33){
								dungeon.setDungeonBlock(x, y, z);
							} else dungeon.setEmptyBlock(x, y, z);
						} else if(!isX && (x == sx + 3 || x == sx + 4)){
							if(dungeon.getRand().nextDouble() < 0.33){
								dungeon.setDungeonBlock(x, y, z);
							} else dungeon.setEmptyBlock(x, y, z);
						} else dungeon.setEmptyBlock(x, y, z);
					} else if(y == start.y - 6){
						world.setBlockId(x, y, z, Vanilla.lava.uid);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
//		Vector3l torch;
//		if(dungeon.getRand().nextDouble() < 0.33){
//			torch = door.left.move(1, door.dir, 1, door.dir.counterClockwise(), 2, Direction.UP);
//			dungeon.setTorchBlock(torch, door.dir.id());
//		}
//		if(dungeon.getRand().nextDouble() < 0.33){
//			torch = door.right.move(1, door.dir, 1, door.dir.clockwise(), 2, Direction.UP);
//			dungeon.setTorchBlock(torch, door.dir.id());
//		}
//		if(dungeon.getRand().nextDouble() < 0.33){
//			torch = door.left.move(move - 1, door.dir, 1, door.dir.counterClockwise(), 2, Direction.UP);
//			dungeon.setTorchBlock(torch, door.dir.opposite().id());
//		}
//		if(dungeon.getRand().nextDouble() < 0.33){
//			torch = door.right.move(move - 1, door.dir, 1, door.dir.clockwise(), 2, Direction.UP);
//			dungeon.setTorchBlock(torch, door.dir.opposite().id());
//		}
		
		return result;
	}

}
