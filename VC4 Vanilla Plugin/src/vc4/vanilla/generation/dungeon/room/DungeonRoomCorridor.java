/**
 * 
 */
package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.generation.dungeon.Door;
import vc4.vanilla.generation.dungeon.Dungeon;
import vc4.vanilla.generation.dungeon.RoomBB;

/**
 * @author paul
 *
 */
public class DungeonRoomCorridor extends DungeonRoom {

	/* (non-Javadoc)
	 * @see vc4.vanilla.generation.dungeon.DungeonRoom#generate(vc4.api.world.World, vc4.vanilla.generation.dungeon.Door, vc4.vanilla.generation.dungeon.Dungeon)
	 */
	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		Vector3l start = door.left;
		start = start.move(2, door.dir.counterClockwise());
		if(!dungeon.inBounds(start)) return result;
		Vector3l end = door.right;
		end = end.move(2, door.dir.clockwise());
		int move = 7 + dungeon.getRand().nextInt(6);
		end = end.move(move, door.dir);
		if(!dungeon.inBounds(end)) return result;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		int xSize = (int) (ex - sx);
		int zSize = (int) (ez - sz);
		RoomBB bb = new RoomBB(sx + 1, start.y, sz + 1, ex - 1, start.y + 3, ez - 1);
		if(!dungeon.addRoom(bb)) return result;
		//System.out.println("X size: " + (ex - sx) + ", Z size: " + (ez - sz));
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 5; ++y){
					if(y == start.y - 1 || y == start.y + 4){
						dungeon.setDungeonBlock(x, y, z);
					} else if(x == sx || x == ex || z == sz || z == ez){
						if((((x == sx + 2 || x == sx + 3) && xSize == 5) || ((z == sz + 2 || z == sz + 3) && zSize == 5) ) && y < start.y + 2 && y > start.y - 1){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						}
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		result.add(door.clone().setNewRoomDir(door.dir.opposite()));
		result.add(door.move(move, door.dir));
		return result;
	}


}
