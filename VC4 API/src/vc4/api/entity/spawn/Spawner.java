package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public interface Spawner {

	public void spawnMob(World world, double x, double y, double z, Random rand);
}
