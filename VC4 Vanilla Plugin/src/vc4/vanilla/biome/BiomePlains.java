package vc4.vanilla.biome;

import java.awt.Color;
import java.util.Random;

import vc4.api.biome.BiomeType;
import vc4.api.generator.GeneratorOutput;

public class BiomePlains extends BiomeHilly {

	int cropChance;
	int emphasisCrop;
	int emphasisChance;
	int[] others;

	public BiomePlains(int id, String name, BiomeType type, Color mapColor, int cropChance, int emphasisCrop, int emphasisChance, int... others) {
		super(id, name, type, mapColor);
		this.cropChance = cropChance;
		this.emphasisCrop = emphasisCrop;
		this.emphasisChance = emphasisChance;
		this.others = others;
	}

	@Override
	public void placeBiomeBlock(GeneratorOutput out, Random rand, int cx, int cy, int cz, long diff, long y) {
		if (diff == -1) {
			if (rand.nextInt(4) != 0) {
				out.setBlockId(cx, cy, cz, getIcingBlock(rand));
				out.setBlockData(cx, cy, cz, 2 + rand.nextInt(4));
			}
			return;
		}
		super.placeBiomeBlock(out, rand, cx, cy, cz, diff, y);
	}

	public int getIcingBlock(Random rand) {
		if (rand.nextInt(cropChance) == 0) {
			if (rand.nextInt(emphasisChance) == 0) {
				return emphasisCrop;
			} else {
				return others[rand.nextInt(others.length)];
			}
		} else return icingBlock;
	}

}
