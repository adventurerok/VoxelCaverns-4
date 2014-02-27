/**
 * 
 */
package vc4.api.block;

import java.util.Random;

import vc4.api.block.render.BlockRendererFluid;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.list.IntList;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;

/**
 * @author paul
 * 
 */
public class BlockFluid extends Block {

	IntList fluidFall = new IntList();
	IntList fluidMove = new IntList();

	int updateWait = 5;

	/**
	 * @param uid
	 * @param texture
	 * @param material
	 */
	public BlockFluid(int uid, int texture, String material) {
		super(uid, texture, material);
		renderer = new BlockRendererFluid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.Block#isSolid(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}

	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return null;
	}

	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return null;
	}

	@Override
	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.block.Block#renderSide(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		return (super.renderSide(world, x, y, z, side) || side == 4) && world.getNearbyBlockId(x, y, z, Direction.getDirection(side)) != uid;
	}

	public double[] getSideHeights(World world, long x, long y, long z) {
		double[] res = new double[4];
		for (int d = 0; d < 4; ++d) {
			res[d] = getSideHeight(world, x, y, z, d);
		}
		return res;
	}

	protected float getBaseFluidSideHeight(byte data) {
		return ((16 - (data % 16))) / 17F;
	}

	protected double getSideHeight(World world, long x, long y, long z, int side) {
		float base = getBaseFluidSideHeight(world.getBlockData(x, y, z));
		if (world.getNearbyBlockId(x, y, z, Direction.UP) == uid) return 1F;
		float amount = 1;
		float total = base;
		Direction dir = Direction.getDirection(side);
		if (world.getNearbyBlockId(x, y + 1, z, dir) == uid) {
			return 1F;
		} else if (world.getNearbyBlockId(x, y, z, dir) == uid) {
			amount += 1;
			total += getBaseFluidSideHeight(world.getNearbyBlockData(x, y, z, dir));
		}
		int sidePlus1 = side + 1;
		if (sidePlus1 == 4) sidePlus1 = 0;
		Direction other = Direction.getDirection(sidePlus1);

		if (world.getNearbyBlockId(x, y + 1, z, other) == uid) {
			return 1F;
		} else if (world.getNearbyBlockId(x, y, z, other) == uid) {
			amount += 1;
			total += getBaseFluidSideHeight(world.getNearbyBlockData(x, y, z, other));
		}
		Direction diag = Direction.getDiagonal(side);
		if (world.getNearbyBlockId(x, y + 1, z, diag) == uid) {
			return 1F;
		} else if (world.getNearbyBlockId(x, y, z, diag) == uid) {
			amount += 1;
			total += getBaseFluidSideHeight(world.getNearbyBlockData(x, y, z, diag));
		}
		base = total / amount;
		return base;
	}

	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		world.setBlockIdData(x, y, z, uid, item.getData());
		item.decrementAmount();
		world.scheduleBlockUpdate(x, y, z, updateWait, 1);
	}

	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		// world.scheduleBlockUpdate(x, y, z, updateWait);
	}

	@Override
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte dat, int buid) {
		return 0;
		// if(dat > 15) return 0;
		// int lvl = (16 - (dat & 15));
		// Block below = world.getBlockType(x, y - 1, z);
		// if(below.isAir){
		// world.setBlockIdData(x, y - 1, z, uid, 16 - lvl);
		// world.setBlockId(x, y, z, 0);
		// return updateWait;
		// }
		// if(below.uid == uid){
		//
		// }
		// fluidMove.clear();
		// fluidFall.clear();
		// for(int d = 0; d < 4; ++d){
		// if(!world.getNearbyBlockType(x, y, z, d).isAir) continue;
		// fluidMove.add(d);
		// if(world.getNearbyBlockType(x, y - 1, z, d).isAir) fluidFall.add(d);
		// }
		// if(!fluidFall.isEmpty()){
		// double div = lvl / (double) (fluidFall.size());
		// int split = (div -((int)div)) > 0.1 ? ((int)(div) + 1) : (int)div;
		// if(split < 2){
		// world.setBlockId(x, y, z, 0);
		// return 0;
		// }
		// for(int d = 0; d < fluidFall.size(); ++d){
		// world.setNearbyBlockIdData(x, y, z, uid, 16 - split, Direction.getDirection(fluidFall.get(d)));
		// }
		// world.setBlockId(x, y, z, 0);
		// return 0;
		// }
		// if(!fluidMove.isEmpty()){
		// double div = lvl / (double) (fluidMove.size() + 1);
		// int split = (div -((int)div)) > 0.1 ? ((int)(div) + 1) : (int)div;
		// if(split < 2){
		// world.setBlockId(x, y, z, 0);
		// return 0;
		// }
		// for(int d = 0; d < fluidMove.size(); ++d){
		// world.setNearbyBlockIdData(x, y, z, uid, 16 - split, Direction.getDirection(fluidMove.get(d)));
		// }
		// world.setBlockData(x, y, z, 16 - split);
		// return 0;
		// }
		// return 0;
	}

}
