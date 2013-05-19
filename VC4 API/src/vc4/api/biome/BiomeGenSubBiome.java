package vc4.api.biome;

import vc4.api.world.World;

public class BiomeGenSubBiome extends ZoomGenerator {

	
	public BiomeGenSubBiome(World world, ZoomGenerator parent) {
		super(world, parent);
	}
	
	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = parent.generate(x, z, size);
		Biome bio;
		int pz;
		for(int px = 0; px < size; ++px){
			for(pz = 0; pz < size; ++pz){
				createRandom(x + px, z + pz);
				bio = Biome.byId(result[pz * size + px]);
				result[pz * size + px] = bio.generateSubBiome(rand);
			}
		}
		return result;
	}

}
