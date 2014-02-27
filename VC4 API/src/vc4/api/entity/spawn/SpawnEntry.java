package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class SpawnEntry {

	SpawnFilter filter;
	Spawner spawner;
	int weight;

	public SpawnEntry(int weight, Spawner spawner, SpawnFilter filter) {
		super();
		this.weight = weight;
		this.spawner = spawner;
		this.filter = filter;
	}

	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return filter.canSpawn(world, x, y, z, rand);
	}

	public void spawnMob(World world, double x, double y, double z, Random rand) {
		spawner.spawnMob(world, x, y, z, rand);
	}

	public int getWeight() {
		return weight;
	}

}
