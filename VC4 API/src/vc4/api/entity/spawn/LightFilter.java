package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class LightFilter implements SpawnFilter {

	int maxLight, minLight;

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		int l = world.getBlockLight(x, y, z);
		return maxLight >= l && minLight <= l;
	}

	public LightFilter(int maxLight, int minLight) {
		super();
		this.maxLight = maxLight;
		this.minLight = minLight;
	}

}
