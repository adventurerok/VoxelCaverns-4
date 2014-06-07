package vc4.vanilla.underbiome;

import vc4.api.biome.ZoomGenerator;

public class BiomeGenUnderBiomes extends ZoomGenerator {

	int[] biomesToGenerate;

	@Override
	public int[] generate(long x, long z, int size) {
		int[] result;
		if (parent != null) result = parent.generate(x, z, size);
		else result = new int[size * size];
		for (int px = 0; px < size; ++px) {
			for (int pz = 0; pz < size; ++pz) {
				initSeed(x + px, z + pz);
				result[pz * size + px] = biomesToGenerate[nextInt(biomesToGenerate.length)];
			}
		}
		return result;
	}

	public BiomeGenUnderBiomes(long worldSeed, int[] biomes) {
		super(worldSeed);
		this.biomesToGenerate = biomes;

	}

}
