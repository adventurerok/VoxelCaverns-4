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
public class DungeonRoomLeftTurn extends DungeonRoom {

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
		end = end.move(3, door.dir);
		if(!dungeon.inBounds(end)) return result;
		long sx = Math.min(start.x, end.x);
		long sz = Math.min(start.z, end.z);
		long ex = Math.max(start.x, end.x);
		long ez = Math.max(start.z, end.z);
		int xSize = (int) (ex - sx);
		int zSize = (int) (ez - sz);
		Vector3l first = start.move(3, door.dir.counterClockwise());
		Vector3l last = end.move(5, door.dir);
		long fx = Math.min(first.x, last.x);
		long fz = Math.min(first.z, last.z);
		long lx = Math.max(first.x, last.x);
		long lz = Math.max(first.z, last.z);
		RoomBB bb = new RoomBB(fx + 1, start.y, fz + 1, lx - 1, start.y + 3, lz - 1);
		if(!dungeon.addRoom(bb)) return result;
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 5; ++y){
					if(y == start.y - 1 || y == start.y + 4){
						dungeon.setDungeonBlock(x, y, z);
						continue;
					} else if(x == sx || x == ex || z == sz || z == ez){
						if(((xSize == 5 && (x == sx + 2 || x == sx + 3)) || (zSize == 5 && (z == sz + 2 || z == sz + 3))) && y < start.y + 2 && y > start.y - 1){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						} else if((xSize == 5 && z == end.z && x != sx && x != ex) || (zSize == 5 && x == end.x && z != sz && z != ez)){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						}
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		start = start.move(4, door.dir);
		end = end.move(5, door.dir);
		sx = Math.min(start.x, end.x);
		sz = Math.min(start.z, end.z);
		ex = Math.max(start.x, end.x);
		ez = Math.max(start.z, end.z);
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 5; ++y){
					if(y == start.y - 1 || y == start.y + 4){
						dungeon.setDungeonBlock(x, y, z);
					} else if(x == end.x || z == end.z){
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		start = start.move(1, door.dir.opposite());
		start = start.move(3, door.dir.counterClockwise());
		end = end.move(5, door.dir.counterClockwise());
		sx = Math.min(start.x, end.x);
		sz = Math.min(start.z, end.z);
		ex = Math.max(start.x, end.x);
		ez = Math.max(start.z, end.z);
		xSize = (int) (ex - sx);
		zSize = (int) (ez - sz);
		for(long x = sx; x <= ex; ++x){
			for(long z = sz; z <= ez; ++z){
				for(long y = start.y - 1; y < start.y + 5; ++y){
					if(y == start.y - 1 || y == start.y + 4){
						dungeon.setDungeonBlock(x, y, z);
						continue;
					} else if(x == sx || x == ex || z == sz || z == ez){
						if(((xSize == 5 && (x == sx + 2 || x == sx + 3)) || (zSize == 5 && (z == sz + 2 || z == sz + 3))) && y < start.y + 2 && y > start.y - 1){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						} else if((xSize == 5 && z == end.z && x != sx && x != ex) || (zSize == 5 && x == end.x && z != sz && z != ez)){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						}
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		result.add(door.clone().setNewRoomDir(door.dir.opposite()));
		result.add(Door.genDoor(door.left.move(5, door.dir.counterClockwise()).move(5, door.dir), door.dir).setNewRoomDir(door.dir.counterClockwise()));
		return result;
	}


}
