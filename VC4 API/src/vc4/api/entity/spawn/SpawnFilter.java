package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public interface SpawnFilter {

	public boolean canSpawn(World world, long x, long y, long z, Random rand);
}
