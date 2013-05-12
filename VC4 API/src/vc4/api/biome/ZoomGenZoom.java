package vc4.api.biome;

import vc4.api.list.IntList;
import vc4.api.world.World;

public class ZoomGenZoom extends ZoomGenerator {

	public ZoomGenZoom(World world, ZoomGenerator parent) {
		super(world, parent);
	}

	@Override
	/*
	 * Zoom in on previous noise. 
	 * All maps store area off the side, for example a 18x18 map is 16x16, 
	 * with index 0 and 17 used for smoothing data
	 * This is works fine when generating limited size maps, but it doesn't work
	 * infinitely
	 */
	public int[] generate(long x, long z, int zoom, int size) {
		int supSize = ((size >> 1) + 2) & -2; //Getting a smaller array from a larger generation
		//int supSize = size;
		int[] sup = parent.generate(x, z, zoom + 1, supSize);
		x = (x >> zoom) << zoom;
		z = (z >> zoom) << zoom;
		int[] result = new int[size * size];
		for (int px = 0; px < size; ++px) {
			int qx = px / 2 + 1;
			int sx = px & 1;
			for (int pz = 0; pz < size; ++pz) {
				createRandom(x + ((px - 1) << (zoom)), z + ((pz - 1) << (zoom)), zoom);
				int qz = pz / 2 + 1;
				int sz = pz & 1;
				IntList its = new IntList();
				its.add(sup[(qx) * supSize + (qz)]);
				if(sx == 1 && px < size - 1) its.add(sup[(qx + 1) * supSize + (qz)]);
				else if(sx == 0 && px > 0) its.add(sup[(qx - 1) * supSize + (qz)]);
				if(sz == 1 && pz < size - 1) its.add(sup[(qx) * supSize + (qz + 1)]);
				else if(sz == 0 && pz > 0) its.add(sup[(qx) * supSize + (qz - 1)]);
				result[px * size + pz] = its.get(rand.nextInt(its.size()));
			}
		}
		return result;
	}

	public int choose(int i1, int i2) {
		return rand.nextBoolean() ? i1 : i2;
	}

	public int choose(int... is) {
		if(is.length == 3) return choose3(is);
		return is[rand.nextInt(is.length)];
	}
	
	public int choose3(int... is){
		if(is[0] == is[2] || is[0] == is[1]) return is[0];
		else if(is[1] == is[2]) return is[1];
		else return is[rand.nextInt(is.length)];
	}
	

}
