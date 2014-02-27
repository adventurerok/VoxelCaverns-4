package vc4.api.entity.spawn;

import java.util.Random;

import vc4.api.biome.Biome;
import vc4.api.list.IntList;
import vc4.api.world.World;

public class BiomeFilter implements SpawnFilter {

	IntList biomeIds = new IntList();

	public BiomeFilter(Biome... biomes) {
		for (Biome b : biomes) {
			biomeIds.add(b.id);
		}
	}

	public BiomeFilter addBiome(Biome b) {
		biomeIds.add(b.id);
		return this;
	}

	public BiomeFilter addBiome(int b) {
		biomeIds.add(b);
		return this;
	}

	@Override
	public boolean canSpawn(World world, long x, long y, long z, Random rand) {
		return biomeIds.contains(world.getBiome(x, z).id);
	}

}
