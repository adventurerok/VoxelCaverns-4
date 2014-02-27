package vc4.api.block;

import vc4.api.world.World;

public interface IBlockJoinable {

	public boolean joinTo(World world, long x, long y, long z, int side);

	public float getDistanceSideToBlock(byte data);
}
