package vc4.vanilla.generation.village;

import java.util.ArrayList;
import java.util.Random;

import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.RoomBB;
import vc4.vanilla.generation.village.style.*;
import vc4.vanilla.npc.Names;

public class Village {

	private static ArrayList<VillageStyle> styles = new ArrayList<>();
	
	long minX, minY, minZ, maxX, maxY, maxZ;
	Random rand;
	World world;

	public ArrayList<RoomBB> rooms = new ArrayList<>();
	public ArrayList<String> lastNames = new ArrayList<>();
	
	byte woodType;
	byte logType;
	byte brickType = 2;
	
	public byte getLogType() {
		return logType;
	}
	
	public byte getWoodType() {
		return woodType;
	}
	
	public byte getBrickType() {
		return brickType;
	}
	
	VillageStyle style;
	
	public VillageStyle getStyle() {
		return style;
	}
	
	static{
		addStyle(new VillageStyleWood());
		addStyle(new VillageStyleStone());
	}
	
	public static void addStyle(VillageStyle style){
		styles.add(style);
	}
	
	public void setRand(Random rand) {
		this.rand = rand;
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
		brickType = 4;
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
	
	public void setTorchBlock(long x, long y, long z){
		int bid = world.getBlockId(x, y - 1, z);
		if (bid == Vanilla.glass.uid || world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4)) {
			world.setBlockIdData(x, y, z, Vanilla.torch.uid, (byte) 0);
			return;
		}
		for (int d = 0; d < 4; ++d) {
			long ox = x + Direction.getDirection(d).getX();
			long oz = z + Direction.getDirection(d).getZ();
			if (world.getBlockType(ox, y, oz).isSolid(world, ox, y, oz, Direction.getOpposite(d).id())) {
				byte data = (byte) (Direction.getOpposite(d).id() + 1);
				world.setBlockIdData(x, y, z, Vanilla.torch.uid, data);
				return;
			}
		}
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
		return Names.generateFirstName(rand);
	}
	
	public String randomLastName(){
		int num = rand.nextInt(lastNames.size() + 1);
		if(num < lastNames.size()) return lastNames.get(num);
		String name = Names.generateLastName(rand);
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
