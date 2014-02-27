package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class RandomFilter implements SpawnFilter {

	double chance;

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return rand.nextDouble() < chance;
	}

	public RandomFilter(double chance) {
		super();
		this.chance = chance;
	}

}
