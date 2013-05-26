/**
 * 
 */
package vc4.api.block;

import vc4.api.block.render.BlockRendererFluid;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class BlockFluid extends Block {

	/**
	 * @param uid
	 * @param texture
	 * @param material
	 */
	public BlockFluid(int uid, int texture, String material) {
		super(uid, texture, material);
		renderer = new BlockRendererFluid();
	}
	
	/* (non-Javadoc)
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
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#renderSide(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		return (super.renderSide(world, x, y, z, side) || side == 4) && world.getNearbyBlockId(x, y, z, Direction.getDirection(side)) != uid;
	}
	
	public double[] getSideHeights(World world, long x, long y, long z){
		double[] res = new double[4];
		for(int d = 0; d < 4; ++d){
			res[d] = getSideHeight(world, x, y, z, d);
		}
		return res;
	}
	
	protected float getBaseFluidSideHeight(byte data) {
		return ((7 - (data % 8))) / 8F;
	}
	
	protected double getSideHeight(World world, long x, long y, long z, int side){
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

}
