package vc4.vanilla.biome;

import java.awt.Color;

import vc4.api.biome.BiomeType;
import vc4.api.biome.FastRandom;
import vc4.vanilla.Vanilla;

public class BiomeDesert extends BiomeHilly {

	public BiomeDesert(int id, String name, BiomeType type, Color mapColor) {
		super(id, name, type, mapColor);

	}

	@Override
	public int generateSubBiome(FastRandom rand, int op) {
		if (op == 0) return super.generateSubBiome(rand, op);
		if (op == 3 && rand.nextInt(20) == 0) return Vanilla.biomeDesertOasis.id;
		return id;
	}

}
