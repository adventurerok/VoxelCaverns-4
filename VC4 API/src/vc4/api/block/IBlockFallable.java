package vc4.api.block;

import vc4.api.world.World;

public interface IBlockFallable {

	
	public void checkForFall(World world, long x, long y, long z);
}
