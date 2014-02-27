package vc4.api.block;

import vc4.api.world.World;

public class BlockPipe extends Block implements IBlockJoinable {

	public BlockPipe(int uid, int texture) {
		super(uid, texture, "pipe");
		// TASK Auto-generated constructor stub
	}

	@Override
	public boolean joinTo(World world, long x, long y, long z, int side) {
		return world.getNearbyBlockType(x, y, z, side) instanceof BlockPipe;
	}

	@Override
	public float getDistanceSideToBlock(byte data) {
		return 0.3125F;
	}

}
