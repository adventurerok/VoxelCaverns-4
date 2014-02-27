package vc4.vanilla.generation.populate;

import java.util.Random;

import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class WorldGenAlgae implements WorldPopulator {

	@Override
	public void populate(World world, long x, long y, long z) {
		if (y != 0) return;
		Random rand = world.createRandom(x, 1827492361832L, z);
		long px = (x << 5) + rand.nextInt(32);
		long pz = (z << 5) + rand.nextInt(32);
		spread(world, rand, px, pz, rand.nextInt(6));
	}

	public void spread(World world, Random rand, long x, long z, int amt) {
		if (amt > 20) return;
		if (!world.getBlockType(x, 0, z).isAir()) return;
		world.setBlockId(x, 0, z, Vanilla.algae.uid);
		if (rand.nextBoolean()) {
			spread(world, rand, x + 1, z, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
			spread(world, rand, x - 1, z, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
			spread(world, rand, x, z + 1, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
			spread(world, rand, x, z - 1, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
		} else {
			spread(world, rand, x, z + 1, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
			spread(world, rand, x, z - 1, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
			spread(world, rand, x + 1, z, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
			spread(world, rand, x - 1, z, amt + 1 + rand.nextInt(rand.nextInt(3) + 1));
		}
	}

}
