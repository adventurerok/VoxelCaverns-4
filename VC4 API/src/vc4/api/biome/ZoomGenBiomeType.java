package vc4.api.biome;

import java.util.ArrayList;

import vc4.api.world.World;

public class ZoomGenBiomeType extends ZoomGenerator {

	private ArrayList<Integer> types = new ArrayList<>();
	
	public ZoomGenBiomeType(World world, ZoomGenerator parent, ArrayList<Integer> types) {
		super(world, parent);	
		this.types = types;
	}
	
	public ZoomGenBiomeType(World world, ZoomGenerator parent){
		super(world, parent);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.cold.id);
		types.add(BiomeType.hot.id);
	}
	
	@Override
	public int[] generate(long x, long z, int zoom, int size) {
		int[] result = parent.generate(x, z, zoom, size);
		x = (x >> zoom) << zoom;
		z = (z >> zoom) << zoom;
		for(int px = 0; px < size; ++px){
			for(int pz = 0; pz < size; ++pz){
				createRandom(x + ((px - 2) << (zoom)), z + ((pz - 2) << (zoom)), zoom);
				if(result[px * size + pz] == 1) result[px * size + pz] = types.get(rand.nextInt(types.size()));
			}
		}
		return result;
	}

}
