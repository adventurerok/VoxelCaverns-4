package vc4.vanilla.biome;

import java.awt.Color;

import vc4.api.biome.*;
import vc4.vanilla.Vanilla;

public class BiomeOcean extends Biome {

	public BiomeOcean(int id) {
		super(id, "ocean", BiomeType.ocean, new Color(30, 144, 255));
	}

	@Override
	public int generateSubBiome(FastRandom rand, int op) {
		if (op == 0 && rand.nextInt(15) == 0) return Vanilla.biomeOceanTrench.id;
		else return id;
	}
}
