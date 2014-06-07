package vc4.api.biome;


public class HeightGenSeed extends ZoomGenerator implements HeightGenBiomeInput {

	int[] biomes;

	@Override
	public int[] generate(long x, long z, int size) {
		int[] result = new int[size * size];
		Biome bio;
		int pz;
		for (int px = 0; px < size; ++px) {
			for (pz = 0; pz < size; ++pz) {
				initSeed(x + px, z + pz);
				bio = Biome.byId(biomes[pz * size + px]);
				result[pz * size + px] = nextInt(bio.diffHeight + 1) + bio.minHeight;
			}
		}
		return result;
	}

	public HeightGenSeed(long worldSeed, ZoomGenerator parent) {
		super(worldSeed, parent);
		// TASK Auto-generated constructor stub
	}

	public HeightGenSeed(long worldSeed) {
		super(worldSeed);
		// TASK Auto-generated constructor stub
	}

	@Override
	public void setBiomes(int[] biomes) {
		this.biomes = biomes;
	}

}
