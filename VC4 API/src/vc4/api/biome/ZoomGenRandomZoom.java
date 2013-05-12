package vc4.api.biome;

import vc4.api.list.IntList;
import vc4.api.world.World;

public class ZoomGenRandomZoom extends ZoomGenerator {

	public ZoomGenRandomZoom(World world, ZoomGenerator parent) {
		super(world, parent);
	}
	
	public static int rootSize = 32;

	@Override
	public int[] generate(long x, long z, int zoom, int size) {
		int supSize = (rootSize >> zoom) + zoom * 2;
		int[] sup = parent.generate(x, z, zoom + 1, supSize);
		x = (x >> zoom) << zoom;
		z = (z >> zoom) << zoom;
		int[] result = new int[size * size];
		for (int px = 0; px < size; ++px) {
			int qx = px / 2 + zoom + 1;
			int sx = (int) ((x + px) & 1);
			for (int pz = 0; pz < size; ++pz) {
				createRandom(x + ((px - zoom) << zoom), z + ((pz - zoom) << zoom), zoom);
				int qz = pz / 2 + zoom + 1;
				int sz = (int) ((z + pz) & 1);
				IntList its = new IntList();
				its.add(sup[(qx) * supSize + (qz)]);
				if(sx == 1 && px < size - zoom) its.add(sup[(qx + 1) * supSize + (qz)]);
				else if(sx == 0 && px > zoom - 2) its.add(sup[(qx - 1) * supSize + (qz)]);
				if(sz == 1 && pz < size - zoom) its.add(sup[(qx) * supSize + (qz + 1)]);
				else if(sz == 0 && pz > zoom - 2) its.add(sup[(qx) * supSize + (qz - 1)]);
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
