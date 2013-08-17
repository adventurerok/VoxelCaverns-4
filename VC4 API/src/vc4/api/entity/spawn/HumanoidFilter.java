package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.world.World;

public class HumanoidFilter implements SpawnFilter {

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return world.getBlockType(x, y, z).isAir() && world.getBlockType(x, y + 1, z).isAir() && world.getBlockType(x, y - 1, z).isSolid(world, x, y - 1, z, 4);
	}

}
