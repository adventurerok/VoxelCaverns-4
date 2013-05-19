package vc4.api.biome;

import java.util.ArrayList;

import vc4.api.world.World;

public class BiomeGenBiome extends ZoomGenerator {

	private ArrayList<ArrayList<Integer>> types = new ArrayList<>();
	
	public BiomeGenBiome(World world, ZoomGenerator parent, ArrayList<ArrayList<Integer>> types) {
		super(world, parent);
		this.types = types;
	}
	
	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = parent.generate(x, z, size);
		ArrayList<Integer> curr;
		int pz;
		for(int px = 0; px < size; ++px){
			for(pz = 0; pz < size; ++pz){
				createRandom(x + px, z + pz);
				curr = types.get(result[pz * size + px]);
				result[pz * size + px] = curr.get(rand.nextInt(curr.size()));
			}
		}
		return result;
	}

}
