package vc4.api.biome;

import vc4.api.biome.ZoomGenerator;
import vc4.api.world.World;

public class BiomeGenIslands extends ZoomGenerator{

	@Override
	public int[] generate(long x, long z, int size) {
		int[] result;
		if(parent != null) result = parent.generate(x, z, size);
		else result = new int[size * size];
		for(int px = 0; px < size; ++px){
			for(int pz = 0; pz < size; ++pz){
				initSeed(x + px, z + pz);
				if(randInt(10) == 0) result[pz * size + px] = 1;
			}
		}
		if (x > -size && x <= 0 && z > -size && z <= 0) result[(int) (-x + -z * size)] = 1;
		return result;
	}

	public BiomeGenIslands(World world, ZoomGenerator parent) {
		super(world, parent);
		// TASK Auto-generated constructor stub
	}

	public BiomeGenIslands(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}

	
}
