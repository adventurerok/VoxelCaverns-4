package vc4.vanilla.generation.village;

import vc4.api.world.World;

public interface Wall {

	public void generate(World world, long x, long z, Village ville);
}
