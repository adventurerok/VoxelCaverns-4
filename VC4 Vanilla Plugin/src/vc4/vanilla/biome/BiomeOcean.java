package vc4.vanilla.biome;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.*;
import vc4.api.block.Block;
import vc4.api.generator.GeneratorOutput;
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
	
	@Override
	public void placeBiomeBlock(GeneratorOutput out, Random rand, int cx, int cy, int cz, long diff, long y) {
		if(y < 0){
			super.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
			return;
		}
		
		long n = diff + (y << 5) + 31 - (31 - cy);
		if(n > 4) out.setBlockId(cx, cy, cz, Block.stone.uid);
		else super.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
	}
}
