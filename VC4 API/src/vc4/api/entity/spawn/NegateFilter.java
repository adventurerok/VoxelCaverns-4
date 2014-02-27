package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class NegateFilter implements SpawnFilter {

	SpawnFilter filter;

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return !filter.canSpawn(world, x, y, z, rand);
	}

	public NegateFilter(SpawnFilter filter) {
		super();
		this.filter = filter;
	}

}
