package vc4.vanilla.generation.populate;

import java.util.Random;

import vc4.api.generator.GeneratorList;
import vc4.api.generator.WorldPopulator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.plant.tree.TreeGen;

public class WorldGenUndergroundClearing implements WorldPopulator {

	@Override
	public void populate(World world, long x, long y, long z) {
		if(y > -4) return;
		Random rand = world.createRandom(x, y, z, 201308162005L);
		x <<= 5;
		y <<= 5;
		z <<= 5;
		for(int d = 0; d < 10; ++d){
			long dx = x + rand.nextInt(32);
			long dy = y + rand.nextInt(32);
			long dz = z + rand.nextInt(32);
			if(world.getBlockId(dx, dy, dz) != 1) continue;
			if(world.getBlockId(dx, dy + 1, dz) != 0) continue;
			world.setBlockId(dx, dy, dz, Vanilla.grass.uid);
			spread(world, dx + 1, dy, dz, 2, 0, rand);
			spread(world, dx - 1, dy, dz, 0, 0, rand);
			spread(world, dx, dy, dz + 1, 3, 0, rand);
			spread(world, dx, dy, dz - 1, 1, 0, rand);
			spread(world, dx, dy - 1, dz, 4, 0, rand);
			TreeGen tree = ((TreeGen)GeneratorList.getPlantGenerator(Vanilla.plantTreeOak));
			tree.setWorld(world);
			tree.rand = rand;
			tree.generate(dx, dy + 1, dz, Vanilla.plantTreeOak);
			tree.setWorld(null);
		}
	}
	
	public void spread(World world, long x, long y, long z, int dir, int amt, Random rand){
		if(amt > 40) return;
		if(world.getBlockId(x, y, z) != 1) return;
		if(world.getBlockId(x, y + 1, z) == 0){
			world.setBlockId(x, y, z, Vanilla.grass.uid);
			if(rand.nextInt(10) == 0) world.setBlockId(x, y + 1, z, Vanilla.lightberries.uid);
			if(dir != 0) spread(world, x + 1, y, z, 2, amt + 1, rand);
			if(dir != 2) spread(world, x - 1, y, z, 0, amt + 1, rand);
			if(dir != 1) spread(world, x, y, z + 1, 3, amt + 1, rand);
			if(dir != 3) spread(world, x, y, z - 1, 1, amt + 1, rand);
			if(dir != 5) spread(world, x, y - 1, z, 4, amt + 1, rand);
			return;
		} else{
			world.setBlockId(x, y, z, Vanilla.dirt.uid);
			if(dir != 0) spread(world, x + 1, y, z, 2, amt + 2, rand);
			if(dir != 2) spread(world, x - 1, y, z, 0, amt + 2, rand);
			if(dir != 1) spread(world, x, y, z + 1, 3, amt + 2, rand);
			if(dir != 3) spread(world, x, y, z - 1, 1, amt + 2, rand);
			if(dir != 4) spread(world, x, y + 1, z, 5, amt + 2, rand);
			if(dir != 5) spread(world, x, y - 1, z, 4, amt + 2, rand);
			return;
		}
	}

}
