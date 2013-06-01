package vc4.vanilla.generation.dungeon.room;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.container.Container;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.*;
import vc4.vanilla.tileentity.TileEntityChest;

public class DungeonRoomLoot extends DungeonRoom {

	@Override
	public Collection<Door> generate(World world, Door door, Dungeon dungeon) {
		ArrayList<Door> result = new ArrayList<>();
		Vector3l start = door.left;
		start = start.move(3, door.dir.counterClockwise());
		if(!dungeon.inBounds(start)) return result;
		Vector3l end = door.right;
		end = end.move(3, door.dir.clockwise());
		end = end.move(7, door.dir);
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
						if((x == sx + 3 || x == sx + 4 || z == sz + 3 || z == sz + 4 ) && y < start.y + 2 && y > start.y - 1){
							dungeon.setEmptyBlock(x, y, z);
							continue;
						}
						dungeon.setDungeonBlock(x, y, z);
					} else dungeon.setEmptyBlock(x, y, z);
				}
			}
		}
		int cx = dungeon.getRand().nextInt(6) + 1;
		int cz = dungeon.getRand().nextInt(6) + 1;
		world.setBlockId(sx + cx, start.y, sz + cz, Vanilla.chest.uid);
		TileEntityChest chestTile = new TileEntityChest(world, new Vector3l(sx + cx, start.y, sz + cz), 0, dungeon.getRand().nextInt(6));
		Container chest = chestTile.getContainer();
		dungeon.getStyle().getLootChest().generate(dungeon.getRand(), chest);
		chestTile.addToWorld();
		result.add(door.clone().setNewRoomDir(door.dir.opposite()));
		return result;
	}

}
