package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.util.Adjustment;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.Dungeon;
import vc4.vanilla.generation.dungeon.RoomBB;

public class DungeonRoomLibrary extends DungeonRoom {

	private static ArrayList<Adjustment> shelves = new ArrayList<>();
	
	static{
		genShelveRow(1, -4);
		genShelveRow(3, -4);
		genShelveRow(8, -4);
		genShelveRow(10, -4);
		genShelveRow(1, 2);
		genShelveRow(3, 2);
		genShelveRow(8, 2);
		genShelveRow(10, 2);
		genShelveRow(5, -1);
		genShelveRow(6, -1);
		genShelvesForward(2, -4);
		genShelvesForward(7, -4);
		genShelvesForward(2, 5);
		genShelvesForward(7, 5);
	}
	
	private static void genShelveRow(long forward, long sideways){
		shelves.add(new Adjustment(forward, sideways, 0));
		shelves.add(new Adjustment(forward, sideways + 1, 0));
		shelves.add(new Adjustment(forward, sideways + 2, 0));
		shelves.add(new Adjustment(forward, sideways + 3, 0));
	}
	
	private static void genShelvesForward(long forward, long sideways){
		shelves.add(new Adjustment(forward, sideways, 0));
		shelves.add(new Adjustment(forward + 1, sideways, 0));
		shelves.add(new Adjustment(forward + 2, sideways, 0));
	}
	
	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		Vector3l start = door.left;
		start = start.move(5, door.dir.counterClockwise());
		if(!dungeon.inBounds(start)) return result;
		Vector3l end = door.right;
		end = end.move(5, door.dir.clockwise());
		end = end.move(11, door.dir);
		if(!dungeon.inBounds(end)) return result;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, start.y + 3, ez - 1);
		if(!dungeon.addRoom(bb)) return result;
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 5; ++y){
					if(y == start.y - 1 || y == start.y + 4){
						dungeon.setDungeonBlock(x, y, z);
					} else if(x == sx || x == ex || z == sz || z == ez){
						if((x == sx + 5 || x == sx + 6 || z == sz + 5 || z == sz + 6 ) && y < start.y + 2 && y > start.y - 1){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						}
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		byte bookshelfData = (byte) dungeon.getRand().nextInt(7);
		for(Adjustment j : shelves){
			Vector3l pos = j.adjust(door.left, door.dir);
			for(int ny = 0; ny < 4; ++ny){
				if(ny < 3){
					world.setBlockIdData(pos.x, pos.y + ny, pos.z, dungeon.getRand().nextInt(75) == 0 ? Vanilla.bookshelfEnchanted.uid : Vanilla.bookshelf.uid, bookshelfData);
				}
				else world.setBlockIdData(pos.x, pos.y + ny, pos.z, Vanilla.planks.uid, bookshelfData);
			}
		}
		result.add(door.clone().setNewRoomDir(door.dir.opposite()));
		result.add(door.move(11, door.dir));
		result.add(Door.genDoor(door.left.move(5, door.dir.counterClockwise()).move(5, door.dir), door.dir).setNewRoomDir(door.dir.counterClockwise()));
		result.add(Door.genDoor(door.right.move(5, door.dir.clockwise()).move(5, door.dir), door.dir).setNewRoomDir(door.dir.clockwise()));
		return result;
	}

}
