package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class DepthFilter implements SpawnFilter {

	long maxY, minY;

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return maxY >= y && minY <= y;
	}

	public DepthFilter(long maxY, long minY) {
		super();
		this.maxY = maxY;
		this.minY = minY;
	}

}
