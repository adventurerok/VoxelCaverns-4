/**
 * 
 */
package vc4.vanilla.generation.dungeon;

import java.util.*;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.loot.LootChest;
import vc4.vanilla.generation.dungeon.style.*;
import vc4.vanilla.npc.Names;
import vc4.vanilla.tileentity.TileEntityChest;

/**
 * @author paul
 * 
 */
public class Dungeon {

	private static ArrayList<DungeonStyle> styles = new ArrayList<>();

	static {
		styles.add(new DungeonStyleStone());
		styles.add(new DungeonStyleLibrary());
	}

	public static void addStyle(DungeonStyle style) {
		styles.add(style);
	}

	long minX, minY, minZ, maxX, maxY, maxZ;
	Random rand;
	World world;

	private boolean generateRoofs = false;
	
	public ArrayList<RoomInfo> rooms = new ArrayList<>();
	public ArrayList<String> lastNames = new ArrayList<>();
	DungeonStyle style;

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
		style = getDungeonStyle(world, minX + maxX / 2, maxY, minZ + maxZ / 2, rand);
	}
	
	public boolean generateRoofs(){
		return generateRoofs;
	}

	public DungeonStyle getStyle() {
		return style;
	}
	
	public boolean randomChance(double chance){
		return getRand().nextDouble() < chance;
	}

	public boolean inBounds(long x, long y, long z) {
		if (x < minX || x > maxX) return false;
		if (y < minY || y > maxY) return false;
		return z >= minZ && z <= maxZ;
	}

	public boolean inBounds(Vector3l pos) {
		if (pos.x < minX || pos.x > maxX) return false;
		if (pos.y < minY || pos.y > maxY) return false;
		return pos.z >= minZ && pos.z <= maxZ;
	}

	public boolean hasRoom(RoomInfo bb) {
		for (RoomInfo b : rooms) {
			if (b.intercepts(bb)) return true;
		}
		return false;
	}
	
	public void clearDoor(Door d){
		world.setBlockId(d.left.x, d.left.y, d.left.z, 0);
		world.setBlockId(d.right.x, d.right.y, d.right.z, 0);
		world.setBlockId(d.left.x, d.left.y + 1, d.left.z, 0);
		world.setBlockId(d.right.x, d.right.y + 1, d.right.z, 0);
	}
	
	public void clearDoors(Collection<Door> doors){
		for(Door d : doors) clearDoor(d);
	}
	
	public void clearDoors(RoomInfo room){
		for(Door d : room.doors) clearDoor(d);
	}
	
	public void genRoom(RoomInfo room){
		long x, y, z;
		boolean xm, zm;
		for(x = room.exterior.minX; x <= room.exterior.maxX; ++x){
			xm = x == room.exterior.minX || x == room.exterior.maxX;
			for(z = room.exterior.minZ; z <= room.exterior.maxZ; ++z){
				zm = z == room.exterior.minX || z == room.exterior.maxX;
				for(y = room.exterior.minX; y < room.exterior.maxX; ++y){
					if(xm || zm || y == room.exterior.minX || y == room.exterior.maxX) setDungeonBlock(x, y, z);
					else setEmptyBlock(x, y, z);
				}
			}
		}
		clearDoors(room);
	}
	
	public void setDungeonBlock(Vector3l pos){
		setDungeonBlock(pos.x, pos.y, pos.z);
	}

	public void setDungeonBlock(long x, long y, long z) {
		if (y < 1 && style.getType() != 14 && world.getBlockId(x, y, z) == 0) return;
		if (rand.nextInt(3) == 0) world.setBlockIdDataNoNotify(x, y, z, style.getMossId(), style.getType());
		else world.setBlockIdDataNoNotify(x, y, z, style.getBrickId(), style.getType());
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param dir The direction of the low end of the stairs from the top end
	 */
	public void setStairBlock(long x, long y, long z, int dir){
		if (y < 1 && style.getType() != 14 && world.getBlockId(x, y, z) == 0) return;
		if(style.getBrickId() == Vanilla.brick.uid){
			int uid = 0;
			int data = (dir & 3);
			if(style.getType() < 8){
				if(style.getType() < 4) uid = Vanilla.brickStairs0.uid;
				else uid = Vanilla.brickStairs4.uid;
			} else if(style.getType() < 12){
				uid = Vanilla.brickStairs8.uid;
			} else uid = Vanilla.brickStairs12.uid;
			data |= (style.getType() & 3) << 2;
			world.setBlockIdDataNoNotify(x, y, z, uid, data);
		}
		
	}

	public void setEmptyBlock(Vector3l pos){
		setEmptyBlock(pos.x, pos.y, pos.z);
	}
	
	public void setEmptyBlock(long x, long y, long z) {
		world.setBlockIdNoNotify(x, y, z, 0);
	}

	public void setTorchBlock(long x, long y, long z) {
		int bid = world.getBlockId(x, y - 1, z);
		if (bid == Vanilla.glass.uid || world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) {
			world.setBlockIdData(x, y, z, Vanilla.torch.uid, (byte) 0);
			return;
		}
		for (int d = 0; d < 4; ++d) {
			long ox = x + Direction.getDirection(d).getX();
			long oz = z + Direction.getDirection(d).getZ();
			if (world.getBlockType(ox, y, oz).isSolid(world, ox, y, oz, Direction.getOpposite(d).id())) {
				int data = Direction.getOpposite(d).id() + 1;
				world.setBlockIdData(x, y, z, Vanilla.torch.uid, data);
				return;
			}
		}
	}
	
	
	/**
	 * 
	 * @param pos
	 * @param dir The direction from the wall to the tip of the torch
	 */
	public void setTorchBlock(Vector3l pos, int dir){
		setTorchBlock(pos.x, pos.y, pos.z, dir);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param dir The direction from the wall to the tip of the torch
	 */
	public void setTorchBlock(long x, long y, long z, int dir) {
		int data = (dir + 1) % 5;
		world.setBlockIdData(x, y, z, Vanilla.torch.uid, data);
	}
	
	public void fillDoors(int id){
		for(RoomInfo i : rooms){
			for(Door d : i.doors){
				fillDoor(d, id);
			}
		}
	}
	
	public void fillDoors(RoomInfo i, int id){
		for(Door d : i.doors){
			fillDoor(d, id);
		}
	}
	
	public void fillDoor(Door d, int id){
		world.setBlockId(d.left.x, d.left.y, d.left.z, id);
		world.setBlockId(d.right.x, d.right.y, d.right.z, id);
		world.setBlockId(d.left.x, d.left.y + 1, d.left.z, id);
		world.setBlockId(d.right.x, d.right.y + 1, d.right.z, id);
	}

	public boolean addRoom(RoomInfo room) {
		if (hasRoom(room)) return false;
		rooms.add(room);
		return true;
	}

	public Random getRand() {
		return rand;
	}

	public World getWorld() {
		return world;
	}

	public String randomFirstName() {
		return Names.generateFirstName(rand);
	}
	
	public TileEntityChest setChestBlock(Vector3l pos){
		return setChestBlock(pos.x, pos.y, pos.z, null);
	}
	
	public TileEntityChest setChestBlock(Vector3l pos, LootChest loot){
		return setChestBlock(pos.x, pos.y, pos.z, loot);
	}
	
	public TileEntityChest setChestBlock(long x, long y, long z){
		return setChestBlock(x, y, z, null);
	}
	
	public TileEntityChest setChestBlock(long x, long y, long z, LootChest loot){
		world.setBlockId(x, y, z, Vanilla.chest.uid);
		TileEntityChest chestTile = new TileEntityChest(world, new Vector3l(x, y, z), 0, getRand().nextInt(6));
		chestTile.addToWorld();
		if(loot != null) loot.generate(getRand(), chestTile.getContainer());
		return chestTile;
	}
	
	public void clearUsedDoors(){
		ArrayList<Door> doors = new ArrayList<>();
		for(RoomInfo i : rooms){
			for(Door d : i.doors){
				doors.add(d);
			}
		}
		int i;
		Door c;
		while(doors.size() > 0){
			c = doors.get(0);
			for(i = 1; i < doors.size(); ++i){
				if(c.fits(doors.get(i))){
					clearDoor(c);
					doors.remove(i);
					break;
				}
			}
			doors.remove(0);
		}
	}

	public String randomLastName() {
		int num = rand.nextInt(lastNames.size() + 1);
		if (num < lastNames.size()) return lastNames.get(num);
		String name = Names.generateLastName(rand);
		lastNames.add(name);
		return name;
	}

	public static DungeonStyle getDungeonStyle(World world, long x, long y, long z, Random rand) {
		ArrayList<DungeonStyle> spawnable = new ArrayList<>();
		for (DungeonStyle d : styles) {
			if (d.canGenerate(world, x, y, z)) spawnable.add(d);
		}
		int max = 0;
		for (DungeonStyle d : spawnable) {
			max += d.getWeight();
		}
		if (max < 1) return null;
		int num = rand.nextInt(max);
		for (DungeonStyle d : spawnable) {
			num -= d.getWeight();
			if (num <= 0) return d;
		}
		return null;
	}

	public static void onWorldLoad(World world) {
		for (DungeonStyle s : styles) {
			s.onWorldLoad(world);
		}
	}
}
