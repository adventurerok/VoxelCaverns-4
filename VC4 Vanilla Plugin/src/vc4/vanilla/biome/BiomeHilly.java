package vc4.vanilla.biome;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.*;

public class BiomeHilly extends Biome {
	
	int hills;

	public BiomeHilly(String name, BiomeType type, Color mapColor) {
		super(name, type, mapColor);
		hills = id;
	}
	
	@Override
	public BiomeHilly setHeights(int max, int min) {
		super.setHeights(max, min);
		return this;
	}
	
	@Override
	public BiomeHilly setHeights(BiomeHeightModel model) {
		super.setHeights(model);
		return this;
	}
	
	public Biome setHills(int hills) {
		this.hills = hills;
		return this;
	}
	
	@Override
	public int generateSubBiome(Random rand) {
		if(rand.nextInt(3) == 0) return hills;
		return id;
	}

}
