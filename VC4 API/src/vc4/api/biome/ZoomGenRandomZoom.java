package vc4.api.biome;

import vc4.api.list.IntList;
import vc4.api.world.World;

public class ZoomGenRandomZoom extends ZoomGenerator {

	public ZoomGenRandomZoom(World world, ZoomGenerator parent) {
		super(world, parent);
	}

	@Override
	public int[] generate(long x, long z, int zoom, int size) {
		int supSize = ((size >> 1) + 3) & -2;
		if(supSize == 18) supSize = 20;
		int[] sup = parent.generate(x, z, zoom + 1, supSize);
		x = (x >> zoom) << zoom;
		z = (z >> zoom) << zoom;
		int[] result = new int[size * size];
		for (int px = 0; px < size; ++px) {
			int qx = px / 2 + 2;
			int sx = px & 1;
			for (int pz = 0; pz < size; ++pz) {
				createRandom(x + ((px - 2) << (zoom - 5)), z + ((pz - 2) << (zoom - 5)), zoom);
				int qz = pz / 2 + 2;
				int sz = pz & 1;
				IntList its = new IntList();
				its.add(sup[(qx) * supSize + (qz)]);
				if(sx == 1 && px > 0 && px < size - 2) its.add(sup[(qx + 1) * supSize + (qz)]);
				else if(sx == 0 && px > 0 && px < size - 2) its.add(sup[(qx - 1) * supSize + (qz)]);
				if(sz == 1 && pz > 0 && pz < size - 2) its.add(sup[(qx) * supSize + (qz + 1)]);
				else if(sz == 0 && pz > 0&& pz < size - 2) its.add(sup[(qx) * supSize + (qz - 1)]);
				result[px * size + pz] = its.get(rand.nextInt(its.size()));
			}
		}
		return result;
	}

	public int choose(int i1, int i2) {
		return rand.nextBoolean() ? i1 : i2;
	}

	public int choose(int... is) {
		if(is.length == 1) return is[0];
		return is[rand.nextInt(is.length)];
	}
	

}
