package vc4.vanilla.biome;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.Biome;
import vc4.api.biome.BiomeType;
import vc4.vanilla.Vanilla;

public class BiomeOcean extends Biome {

	
	public BiomeOcean(int id) {
		super(id, "ocean", BiomeType.ocean, Color.blue);
	}
	
	@Override
	public int generateSubBiome(Random rand, int op) {
		if(rand.nextInt(5) == 0 && op == 0) return Vanilla.biomeTrench.id;
		else return id;
	}
}
