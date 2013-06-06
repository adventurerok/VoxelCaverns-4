package vc4.vanilla.generation.village;

import java.util.ArrayList;
import java.util.Random;

import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.NpcNames;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.style.VillageStyle;
import vc4.vanilla.generation.village.style.VillageStyleBasic;

public class Village {

	private static ArrayList<VillageStyle> styles = new ArrayList<>();
	
	long minX, minY, minZ, maxX, maxY, maxZ;
	Random rand;
	World world;

	public ArrayList<RoomBB> rooms = new ArrayList<>();
	public ArrayList<String> lastNames = new ArrayList<>();
	
	byte woodType;
	byte logType;
	byte brickType;
	
	VillageStyle style;
	
	public VillageStyle getStyle() {
		return style;
	}
	
	static{
		addStyle(new VillageStyleBasic());
	}
	
	public static void addStyle(VillageStyle style){
		styles.add(style);
	}
	
	public static VillageStyle getVillageStyle(World world, long x, long y, long z, Random rand){
		ArrayList<VillageStyle> spawnable = new ArrayList<>();
		for(VillageStyle d : styles){
			if(d.canGenerate(world, x, y, z)) spawnable.add(d);
		}
		int max = 0;
		for(VillageStyle d : spawnable){
			max += d.getWeight();
		}
		if(max < 1) return null;
		int num = rand.nextInt(max);
		for(VillageStyle d : spawnable){
			num -= d.getWeight();
			if(num <= 0) return d;
		}
		return null;
	}

	public Village(World world, long minX, long minY, long minZ, long maxX, long maxY, long maxZ, Random rand) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.rand = rand;
		this.world = world;
		woodType = (byte) rand.nextInt(7);
		logType = (byte) rand.nextInt(7);
		brickType = 0;
		style = getVillageStyle(world, minX + maxX  / 2, maxY, minZ + maxZ / 2, rand);
	}
	
	public void setLogBlock(long x, long y, long z){
		world.setBlockIdDataNoNotify(x, y, z, Vanilla.logV.uid, logType);
	}
	
	public void setCobbleBlock(long x, long y, long z){
		world.setBlockIdDataNoNotify(x, y, z, Vanilla.brick.uid, 15);
	}
	
	public void setGlassBlock(long x, long y, long z){
		world.setBlockIdDataNoNotify(x, y, z, Vanilla.glass.uid, 0);
	}
	
	public void setBrickBlock(long x, long y, long z){
		world.setBlockIdDataNoNotify(x, y, z, Vanilla.brick.uid, brickType);
	}
	
	public void setPlankBlock(long x, long y, long z){
		world.setBlockIdDataNoNotify(x, y, z, Vanilla.planks.uid, woodType);
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
	
	public String randomFirstName(){
		return NpcNames.getRandomFirst(getRand());
	}
	
	public String randomLastName(){
		int num = rand.nextInt(lastNames.size() + 1);
		if(num < lastNames.size()) return lastNames.get(num);
		String name = NpcNames.getRandomLast(getRand());
		lastNames.add(name);
		return name;
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
}
