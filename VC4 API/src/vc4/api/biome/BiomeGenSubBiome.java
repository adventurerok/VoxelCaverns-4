package vc4.api.biome;

import vc4.api.world.World;

public class BiomeGenSubBiome extends ZoomGenerator {

	int op;
	
	public BiomeGenSubBiome(World world, ZoomGenerator parent, int op) {
		super(world, parent);
		this.op = op;
	}
	
	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = parent.generate(x, z, size);
		Biome bio;
		int pz;
		for(int px = 0; px < size; ++px){
			for(pz = 0; pz < size; ++pz){
				initSeed(x + px, z + pz);
				bio = Biome.byId(result[pz * size + px]);
				result[pz * size + px] = bio.generateSubBiome(this, op);
			}
		}
		return result;
	}

}
