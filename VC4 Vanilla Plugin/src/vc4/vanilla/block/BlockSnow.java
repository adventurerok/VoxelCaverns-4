package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockSnow extends Block {

	public BlockSnow(int uid) {
		super(uid, BlockTexture.grassTop, "snow");
	}
	
	@Override
	public AABB getRenderSize(World world, long x, long y, long z) {
		double height = 0.125 * (world.getBlockData(x, y, z) + 1);
		return AABB.getBoundingBox(0, 1, 0, height, 0, 1);
	}
	
	@Override
	public boolean canBeReplaced(int id, byte data) {
		return id != uid && id != 0;
	}
	
	@Override
	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return bid != uid && bid != 0;
	}
	
	
	@Override
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		return null;
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		double height = 0.125 * (world.getBlockData(x, y, z) + 1);
		return AABB.getBoundingBox(0, 1, 0, height, 0, 1);
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#getRendererToUse(byte, int)
	 */
	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.block.Block#renderSide(vc4.api.world.World, long, long, long, int)
	 */
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		if(side < 4) return world.getNearbyBlockId(x, y, z, Direction.getDirection(side)) != uid || world.getBlockData(x, y, z) >= world.getNearbyBlockData(x, y, z, Direction.getDirection(side));
		else if(side == 4) return true;
		else return false;
	}

}
