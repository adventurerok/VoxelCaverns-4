package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.util.Adjustment;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.*;
import vc4.vanilla.generation.furnature.Furnature;
import vc4.vanilla.generation.furnature.FurnatureBasic;

public class DungeonRoomClimb extends DungeonRoom {

	private static ArrayList<Furnature> steps = new ArrayList<>();
	private static String lastWorld = "";
	
	private static void loadFurnature(){
		steps.clear();
		steps.add(new FurnatureBasic(new Adjustment(2, 2, 0), Vanilla.brickHalf.uid, 4));
		steps.add(new FurnatureBasic(new Adjustment(2, 1, 0), Vanilla.brickHalf.uid, 20));
		steps.add(new FurnatureBasic(new Adjustment(2, 0, 1), Vanilla.brickHalf.uid, 4));
		steps.add(new FurnatureBasic(new Adjustment(2, -1, 1), Vanilla.brickHalf.uid, 20));
		steps.add(new FurnatureBasic(new Adjustment(3, -1, 2), Vanilla.brickHalf.uid, 4));
		steps.add(new FurnatureBasic(new Adjustment(4, -1, 2), Vanilla.brickHalf.uid, 20));
		steps.add(new FurnatureBasic(new Adjustment(5, -1, 3), Vanilla.brickHalf.uid, 4));
		steps.add(new FurnatureBasic(new Adjustment(5, 0, 3), Vanilla.brickHalf.uid, 20));
		steps.add(new FurnatureBasic(new Adjustment(5, 1, 4), Vanilla.brickHalf.uid, 4));
		
		//air
		steps.add(new FurnatureBasic(new Adjustment(2, -1, 4), 0, 0));
		steps.add(new FurnatureBasic(new Adjustment(3, -1, 4), 0, 0));
		steps.add(new FurnatureBasic(new Adjustment(4, -1, 4), 0, 0));
		steps.add(new FurnatureBasic(new Adjustment(5, -1, 4), 0, 0));
		steps.add(new FurnatureBasic(new Adjustment(5, 0, 4), 0, 0));
	}
	
	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		
		if(dungeon.getRand().nextBoolean()){
			door = door.move(5, Direction.DOWN);
		}
		
		ArrayList<Door> result = new ArrayList<>();
		result.add(door.flip());
		result.add(door.move(11, door.dir));
		result.add(Door.genDoor(door.left.move(5, door.dir.counterClockwise()).move(5, door.dir), door.dir));
		result.add(Door.genDoor(door.right.move(5, door.dir.clockwise()).move(5, door.dir), door.dir));
		
		result.add(door.flip().move(5, Direction.UP));
		result.add(door.move(11, door.dir).move(5, Direction.UP));
		result.add(Door.genDoor(door.left.move(5, door.dir.counterClockwise()).move(5, door.dir), door.dir).move(5, Direction.UP));
		result.add(Door.genDoor(door.right.move(5, door.dir.clockwise()).move(5, door.dir), door.dir).move(5, Direction.UP));
		
		Vector3l start = door.left;
		start = start.move(5, door.dir.counterClockwise());
		if (!dungeon.inBounds(start)) return null;
		
		Vector3l end = door.right;
		end = end.move(5, door.dir.clockwise());
		end = end.move(11, door.dir);
		if (!dungeon.inBounds(end)) return null;
		
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, start.y + 8, ez - 1);
		RoomInfo info = new RoomInfo(bb, result);
		if (!dungeon.addRoom(info)) return null;
		
		if(!lastWorld.equals(world.getSaveName())){
			lastWorld = world.getSaveName();
			loadFurnature();
		}
		
		for (long x = sx; x <= ex; ++x) {
			for (long z = sz; z <= ez; ++z) {
				for (long y = start.y - 1; y < start.y + 10; ++y) {
					if (y == start.y - 1 || y == start.y + 4 || y == start.y + 9) {
						dungeon.setDungeonBlock(x, y, z);
					} else if (x == sx || x == ex || z == sz || z == ez) {
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		
		Vector3l placeStart = door.left.move(2, door.dir);
		
		for(Furnature f : steps){
			f.place(world, placeStart, door.dir);
		}
		
		return result;
	}

}
