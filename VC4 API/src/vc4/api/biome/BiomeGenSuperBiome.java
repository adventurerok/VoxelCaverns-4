package vc4.api.biome;

import java.util.ArrayList;

public class BiomeGenSuperBiome extends ZoomGenerator {

	private ArrayList<Integer> types = new ArrayList<>();

	public BiomeGenSuperBiome(long worldSeed, ZoomGenerator parent, ArrayList<Integer> types) {
		super(worldSeed, parent);
		this.types = types;
	}

	public BiomeGenSuperBiome(long worldSeed, ZoomGenerator parent) {
		super(worldSeed, parent);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.normal.id);
		types.add(BiomeType.cold.id);
		types.add(BiomeType.hot.id);
		types.add(BiomeType.hot.id);
	}

	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = parent.generate(x, z, size);
		int pz;
		for (int px = 0; px < size; ++px) {
			for (pz = 0; pz < size; ++pz) {
				initSeed(x + px, z + pz);
				if (result[pz * size + px] == 1) result[pz * size + px] = types.get(nextInt(types.size()));
			}
		}
		return result;
	}

}
