package vc4.api.biome;

import vc4.api.biome.ZoomGenerator;
import vc4.api.world.World;

public class ZoomGenIslands extends ZoomGenerator{

	@Override
	public int[] generate(long x, long z, int zoom, int size) {
		int[] result;
		if(parent != null) result = parent.generate(x, z, zoom, size);
		else result = new int[size * size];
		x = (x >> zoom) << zoom;
		z = (z >> zoom) << zoom;
		for(int px = 0; px < size; ++px){
			for(int pz = 0; pz < size; ++pz){
				createRandom(x + ((px - zoom) << zoom), z + ((pz - zoom) << zoom), zoom);
				if(rand.nextInt(5) == 0) result[px * size + pz] = 1;
			}
		}
		return result;
	}

	public ZoomGenIslands(World world, ZoomGenerator parent) {
		super(world, parent);
		// TASK Auto-generated constructor stub
	}

	public ZoomGenIslands(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}

	
}
