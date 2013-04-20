/**
 * 
 */
package vc4.vanilla.generation.dungeon;

import java.util.ArrayList;
import java.util.Random;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

/**
 * @author paul
 * 
 */
public class Dungeon {

	long minX, minY, minZ, maxX, maxY, maxZ;
	Random rand;
	World world;
	byte type;

	public ArrayList<RoomBB> rooms = new ArrayList<>();

	public Dungeon(World world, long minX, long minY, long minZ, long maxX, long maxY, long maxZ, Random rand) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.rand = rand;
		this.world = world;
		type = (byte) (rand.nextBoolean() ? 4 : 15);
		if(minY < -4900 && rand.nextBoolean()) type = 14;
	}
	
	public boolean inBounds(long x, long y, long z){
		if(x < minX || x > maxX) return false;
		if(y < minY || y > maxY) return false;
		return z >= minZ && z <= maxZ;
	}
	
	public boolean inBounds(Vector3l pos){
		if(pos.x < minX || pos.x > maxX) return false;
		if(pos.y < minY || pos.y > maxY) return false;
		return pos.z >= minZ && pos.z <= maxZ;
	}

	public boolean hasRoom(RoomBB bb) {
		for(RoomBB b : rooms){
			if(b.intercepts(bb)) return true;
		}
		return false;
	}
	
	public void setDungeonBlock(long x, long y, long z){
		if(type != 14 && world.getBlockId(x, y, z) == 0) return;
		if(rand.nextInt(3) == 0) world.setBlockIdDataNoNotify(x, y, z, Vanilla.mossBrick.uid, type);
		else world.setBlockIdDataNoNotify(x, y, z, Vanilla.brick.uid, type);
	}
	
	public void setEmptyBlock(long x, long y, long z){
		world.setBlockIdNoNotify(x, y, z, 0);
	}
	
	public boolean addRoom(RoomBB bb){
		if(hasRoom(bb)) return false;
		rooms.add(bb);
		return true;
	}
	
	public Random getRand() {
		return rand;
	}
	
	public World getWorld() {
		return world;
	}
}
