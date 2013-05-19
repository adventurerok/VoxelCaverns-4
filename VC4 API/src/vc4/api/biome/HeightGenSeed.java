package vc4.api.biome;

import vc4.api.biome.ZoomGenerator;
import vc4.api.world.World;

public class HeightGenSeed extends ZoomGenerator implements HeightGenBiomeInput{

	int[] biomes;
	
	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = new int[size * size];
		Biome bio;
		int pz;
		for(int px = 0; px < size; ++px){
			for(pz = 0; pz < size; ++pz){
				createRandom(x + px, z + pz);
				bio = Biome.byId(biomes[pz * size + px]);
				result[pz * size + px] = rand.nextInt(bio.diffHeight) + bio.minHeight;
			}
		}
		return result;
	}

	public HeightGenSeed(World world, ZoomGenerator parent) {
		super(world, parent);
		// TASK Auto-generated constructor stub
	}

	public HeightGenSeed(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}
	
	@Override
	public void setBiomes(int[] biomes) {
		this.biomes = biomes;
	}
	


	
}
