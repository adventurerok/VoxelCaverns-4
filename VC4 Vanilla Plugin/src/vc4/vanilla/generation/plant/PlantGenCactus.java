package vc4.vanilla.generation.plant;

import java.util.Random;

import vc4.api.block.Plant;
import vc4.api.generator.PlantGenerator;
import vc4.api.world.MapData;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class PlantGenCactus implements PlantGenerator {

	World world;
	Random rand;
	
	@Override
	public void growPlant(World world, MapData data, long x, long y, long z, Random rand, Plant plant) {
		if(y < 0) return;
		int cx = rand.nextInt(32);
		int cz = rand.nextInt(32);
		long height = data.getHeight(cx, cz);
		if(height >> 5 != y) return;
		this.world = world;
		this.rand = rand;
		generate((x << 5) + cx, height + 1, (z << 5) + cz, plant);

	}
	
	public void generate(long x, long y, long z, Plant plant){
		if(!world.getBlockType(x, y - 1, z).canGrowPlant(plant)) return;
		int max = rand.nextInt(3) == 0 ? 4 : 3;
		for(int d = 0; d < max; ++d){
			if(!world.getBlockType(x, y + d, z).canBeReplaced(Vanilla.cactus.uid, (byte) 0)) return;
			world.setBlockId(x, y + d, z, Vanilla.cactus.uid);
			if(rand.nextInt(3) == 0) return;
		}
	}

	@Override
	public void onWorldLoad(World world) {
		
	}

}
