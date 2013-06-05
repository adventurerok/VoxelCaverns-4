package vc4.vanilla.generation.plant.tree;

import java.util.Random;

import vc4.api.block.Block;
import vc4.api.block.Plant;
import vc4.api.generator.PlantGenerator;
import vc4.api.util.Direction;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public abstract class TreeGen implements PlantGenerator{

	protected World world;
	public Random rand;
	
	public TreeGen() {
		// TASK Auto-generated constructor stub
	}
	
	
	
	public TreeGen(World world, Random rand) {
		super();
		this.world = world;
		this.rand = rand;
	}

	
	public void setBlockVine(long x, long y, long z){
		if(!world.getBlockType(x, y, z).replacableBy(world, x, y, z, getVineBlockId(), (byte)0)) return;
		boolean ret = true;
		if (world.getBlockId(x, y + 1, z) == getVineBlockId()) ret = false;
		for (int d = 0; d < 4 && ret == true; ++d) {
			if (vineCheckNearby(world, x, y, z, d)) ret = false;
		}
		if (ret) return;
		int dat = 0;
		for (int d = 0; d < 4; ++d) {
			if (vineCheckNearby(world, x, y, z, d)){
				dat |= 1 << d;
			}
		}
		if(world.getBlockId(x, y + 1, z) == getVineBlockId()){
			dat |= world.getBlockData(x, y + 1, z);
		}
		world.setBlockIdData(x, y, z, getVineBlockId(), dat);
	}
	
	private boolean vineCheckNearby(World world, long x, long y, long z, int side){
		Block b = world.getNearbyBlockType(x, y, z, Direction.getDirection(side));
		return b.uid == Vanilla.leaf.uid || b.isSolid(world, x, y, z, Direction.getOpposite(side).getId());
	}
	
	public int getVineBlockId(){
		return Vanilla.vines.uid;
	}


	public abstract boolean generate(long x, long y, long z, Plant plant);
	
	@Override
	public void growPlant(World world, MapData data, long x, long y, long z, Random rand, Plant plant) {
		if(y < 0) return;
		int cx = rand.nextInt(32);
		int cz = rand.nextInt(32);
		long height = data.getHeight(cx, cz);
		if(height >> 5 != y) return;
		this.world = world;
		this.rand = rand;
		generate((x << 5) + cx, height + 1, (z << 5) + cz, plant);
	}
	
	@Override
	public void onWorldLoad(World world) {
	}
	
	protected void setBlockAsLeaf(long x, long y, long z, byte meta){
		int i = world.getBlockId(x, y, z);
		if(i == 0 || Block.byId(i).replacableBy(world, x, y, z, Vanilla.leaf.uid, meta)){
			world.setBlockIdDataNoNotify(x, y, z, Vanilla.leaf.uid, meta + 16);
		}
	}
	protected void setBlockAsLeafWithChance(long x, long y, long z, byte meta, int pc){
		if(rand.nextInt(100) < pc) setBlockAsLeaf(x, y, z, meta);
	}
	
	protected void setBlockAsWood(long x, long y, long z, byte meta){
		internalWoodSet(x, y, z, Vanilla.logV.uid, meta);
	}
	protected void setBlockAsWoodX(long x, long y, long z, byte meta){
		internalWoodSet(x, y, z, Vanilla.logX.uid, meta);
	}
	protected void setBlockAsWoodZ(long x, long y, long z, byte meta){
		internalWoodSet(x, y, z, Vanilla.logZ.uid, meta);
	}
	protected void fillBlocksWithWood(long minX, long minY, long minZ, long maxX, long maxY, long maxZ, byte meta){
		long sx = Math.min(minX, maxX);
		long sy = Math.min(minY, maxY);
		long sz = Math.min(minZ, maxZ);
		long ex = Math.max(minX, maxX) + 1;
		long ey = Math.max(minY, maxY) + 1;
		long ez = Math.max(minZ, maxZ) + 1;
		for(long nx = sx; nx < ex; ++nx){
			for(long ny = sy; ny < ey; ++ny){
				for(long nz = sz; nz < ez; ++nz){
					internalWoodSet(nx, ny, nz, Vanilla.logV.uid, meta);
				}
			}
		}
	}
	private void internalWoodSet(long x, long y, long z, short id, byte meta){
		Block i = world.getBlockType(x, y, z);
		if(i.uid == Vanilla.leaf.uid || i.replacableBy(world, x, y, z, id, meta)){
			world.setBlockIdDataNoNotify(x, y, z, id, meta + 16);
		}
	}
	protected void setBlockAsWoodRelative(long x, long y, long z, int side, byte meta){
		Direction dir = Direction.getDirection(side);
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockAsWood(x, y, z, meta);
	}
	protected void setBlockAsLeafRelative(long x, long y, long z, int side, byte meta){
		Direction dir = Direction.getDirection(side);
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockAsLeaf(x, y, z, meta);
	}
}
