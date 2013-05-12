package vc4.api.biome;

import java.util.ArrayList;

import vc4.api.world.World;

public class ZoomGenBiome extends ZoomGenerator {

	private ArrayList<ArrayList<Integer>> types = new ArrayList<>();
	
	public ZoomGenBiome(World world, ZoomGenerator parent, ArrayList<ArrayList<Integer>> types) {
		super(world, parent);
		this.types = types;
	}
	
	@Override
	public int[] generate(long x, long z, int zoom, int size) {
		int[] result = parent.generate(x, z, zoom, size);
		x = (x >> zoom) << zoom;
		z = (z >> zoom) << zoom;
		for(int px = 0; px < size; ++px){
			for(int pz = 0; pz < size; ++pz){
				createRandom(x + ((px - 1) << (zoom)), z + ((pz - 1) << (zoom)), zoom);
				ArrayList<Integer> curr = types.get(result[px * size + pz]);
				result[px * size + pz] = curr.get(rand.nextInt(curr.size()));
			}
		}
		return result;
	}

}
