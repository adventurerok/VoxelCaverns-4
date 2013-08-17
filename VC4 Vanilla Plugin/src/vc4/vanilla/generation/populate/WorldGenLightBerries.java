package vc4.vanilla.generation.populate;

import java.util.Random;

import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class WorldGenLightBerries implements WorldPopulator {

	@Override
	public void populate(World world, long x, long y, long z) {
		if(y > -4) return;
		Random rand = world.createRandom(x, y, z, 201308161939L);
		x <<= 5;
		y <<= 5;
		z <<= 5;
		for(int d = 0; d < 14; ++d){
			long dx = x + rand.nextInt(32);
			long dy = y + rand.nextInt(32);
			long dz = z + rand.nextInt(32);
			if(world.getBlockId(dx, dy, dz) != 0) continue;
			if(!world.getBlockType(dx, dy + 1, dz).isSolid(world, dx, dy + 1, dz, 5) && !world.getBlockType(dx, dy - 1, dz).isSolid(world, dx, dy - 1, dz, 4)) continue;
			world.setBlockId(dx, dy, dz, Vanilla.lightberries.uid);
		}
	}

}
