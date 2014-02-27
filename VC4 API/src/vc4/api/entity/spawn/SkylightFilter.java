package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class SkylightFilter implements SpawnFilter {

	boolean light;

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return world.hasDayLight(x, y, z) == light;
	}

	public SkylightFilter(boolean light) {
		super();
		this.light = light;
	}

}
