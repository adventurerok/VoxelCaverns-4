package vc4.vanilla.biome;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.BiomeType;
import vc4.api.generator.GeneratorOutput;
import vc4.vanilla.Vanilla;

public class BiomeForestBamboo extends BiomeHilly {

	public BiomeForestBamboo(int id, String name, BiomeType type, Color mapColor) {
		super(id, name, type, mapColor);
		// TASK Auto-generated constructor stub
	}

	@Override
	public void placeBiomeBlock(GeneratorOutput out, Random rand, int cx, int cy, int cz, long diff, long y) {
		if (diff == -1) {
			out.setBlockId(cx, cy, cz, Vanilla.reeds.uid);
			out.setBlockData(cx, cy, cz, 2);
		} else if (diff == 0) {
			if ((y << 5) + cy < -1) out.setBlockId(cx, cy, cz, fillerBlock);
			else out.setBlockId(cx, cy, cz, topBlock);
		} else if (diff < 5) out.setBlockId(cx, cy, cz, fillerBlock);
		else out.setBlockId(cx, cy, cz, bottomBlock);
	}

}
