package vc4.api.biome;

import java.util.ArrayList;

import vc4.api.world.World;

public class BiomeGenSuperbiome extends ZoomGenerator {

	private ArrayList<Integer> types = new ArrayList<>();
	
	public BiomeGenSuperbiome(World world, ZoomGenerator parent, ArrayList<Integer> types) {
		super(world, parent);	
		this.types = types;
	}
	
	public BiomeGenSuperbiome(World world, ZoomGenerator parent){
		super(world, parent);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.cold.id);
		types.add(BiomeType.hot.id);
	}
	
	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = parent.generate(x, z, size);
		for(int px = 0; px < size; ++px){
			for(int pz = 0; pz < size; ++pz){
				createRandom(x + px, z + pz);
				if(result[pz * size + px] == 1) result[pz * size + px] = types.get(rand.nextInt(types.size()));
			}
		}
		return result;
	}

}
