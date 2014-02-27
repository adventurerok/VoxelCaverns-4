package vc4.vanilla.biome;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.BiomeType;
import vc4.api.block.Block;
import vc4.api.generator.GeneratorOutput;
import vc4.vanilla.Vanilla;

public class BiomeVolcanic extends BiomeHilly {

	int amt;

	public BiomeVolcanic(int id, String name, BiomeType type, Color mapColor, int amt) {
		super(id, name, type, mapColor);
		this.amt = amt;
		soilDepth = amt * 3;
	}

	@Override
	public void placeBiomeBlock(GeneratorOutput out, Random rand, int cx, int cy, int cz, long diff, long y) {
		int type = Block.stone.uid;
		if (rand.nextInt(amt) != 0) type = Vanilla.obsidian.uid;
		if (diff == 0) {
			out.setBlockId(cx, cy, cz, type);
		} else if (diff < 5) out.setBlockId(cx, cy, cz, type);
		else out.setBlockId(cx, cy, cz, type);
	}

}
