package vc4.vanilla.generation.plant;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.generator.PlantGenerator;
import vc4.api.world.MapData;
import vc4.api.world.World;

public class PlantGenCrops implements PlantGenerator {

	World world;
	Random rand;
	int id;

	public PlantGenCrops(int id) {
		this.id = id;
	}

	@Override
	public void growPlant(World world, MapData data, long x, long y, long z, Random rand, Plant plant) {
		if (y < 0) return;
		int cx = rand.nextInt(32);
		int cz = rand.nextInt(32);
		long height = data.getGenHeight(cx, cz);
		if (height >> 5 != y) return;
		this.world = world;
		this.rand = rand;
		generate((x << 5) + cx, height + 1, (z << 5) + cz, plant);

	}

	public void generate(long x, long y, long z, Plant plant) {
		if (!world.getBlockType(x, y - 1, z).canGrowPlant(plant)) return;
		if (!world.getBlockType(x, y, z).canBeReplaced(id, (byte) 0)) return;
		world.setBlockIdData(x, y, z, id, rand.nextInt(7));
	}

	@Override
	public void onWorldLoad(World world) {
	}

}
