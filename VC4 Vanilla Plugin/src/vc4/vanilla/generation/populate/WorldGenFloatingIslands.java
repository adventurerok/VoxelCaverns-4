package vc4.vanilla.generation.populate;

import java.util.Random;

import vc4.api.generator.WorldPopulator;
import vc4.api.util.noise.SimplexOctaveGenerator;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;

public class WorldGenFloatingIslands implements WorldPopulator {

	@Override
	public void populate(World world, long x, long y, long z) {
		Random rand = world.createRandom(x, y, z, 5648613);
		if (rand.nextInt(25) != 0) return;
		SimplexOctaveGenerator noise = new SimplexOctaveGenerator(rand.nextLong(), 1);
		noise.setScale(1 / 30d);
		long px = (x << 5) + rand.nextInt(32);
		long py = (y << 5) + rand.nextInt(32);
		long pz = (z << 5) + rand.nextInt(32);
		int radius = 12 + rand.nextInt(12);
		int size = radius + 4;
		double large = size / (double) (radius * radius);
		int cz, yFact, yMax, yMin, cy;
		double xzSq;
		for (int cx = -size; cx <= size; ++cx) {
			for (cz = -size; cz <= size; ++cz) {
				yFact = 0;
				xzSq = (cx * cx + cz * cz) * large;
				yMax = (int) Math.abs(noise.noise(px + cx, pz + cz, py ^ 3124781794L, 0.1, 1) * 5);
				yMin = (int) ((-size + 3 + xzSq) + Math.abs(noise.noise(px + cx, pz + cz, py ^ 18372517L, 3, 3, true)));
				if (yMin > yMax) continue;
				for (cy = yMax; cy >= yMin; --cy) {
					// double sq = (cx * cx + cz * cz) * 1.5 + cy * cy;
					// if ((sq / large - 1) > noise.noise(cx + px, cy + py, cz + pz, 0.1, 1d, true)){
					// yFact = 0;
					// continue;
					// }
					if (yFact == 0) world.setBlockId(cx + px, cy + py, cz + pz, Vanilla.grass.uid);
					else if (yFact < 4) world.setBlockId(cx + px, cy + py, cz + pz, Vanilla.dirt.uid);
					else world.setBlockId(cx + px, cy + py, cz + pz, 1);
					yFact++;
				}
			}

		}

	}
}
