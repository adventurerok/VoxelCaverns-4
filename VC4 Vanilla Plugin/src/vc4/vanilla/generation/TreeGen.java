package vc4.vanilla.generation;

import java.util.Random;

import vc4.api.block.Block;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public abstract class TreeGen {

	protected World world;
	protected Random rand;
	public TreeGen(World world, Random rand) {
		super();
		this.world = world;
		this.rand = rand;
	}
	
	public abstract boolean generate(long x, long y, long z, int tt);
	
	protected void setBlockAsLeaf(long x, long y, long z, byte meta){
		int i = world.getBlockId(x, y, z);
		if(i == 0 || Block.byId(i).replacableBy(world, x, y, z, Vanilla.leaf.uid, meta)){
			world.setBlockIdData(x, y, z, Vanilla.leaf.uid, meta + 16);
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
			world.setBlockIdData(x, y, z, id, meta + 16);
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
