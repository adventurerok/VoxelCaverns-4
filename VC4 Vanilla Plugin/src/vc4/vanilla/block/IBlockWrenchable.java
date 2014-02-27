package vc4.vanilla.block;

import vc4.api.world.World;

public interface IBlockWrenchable {

	public void wrench(World world, long x, long y, long z, int side, int mouseButton);
}
