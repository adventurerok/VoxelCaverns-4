package vc4.vanilla.underbiome;

import vc4.api.biome.ZoomGenerator;
import vc4.api.world.World;

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

	public BiomeGenUnderBiomes(World world, int[] biomes) {
		super(world);
		this.biomesToGenerate = biomes;

	}

}
