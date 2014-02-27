package vc4.api.biome;

import vc4.api.math.MathUtils;
import vc4.api.world.World;

public class HeightGenZoom extends ZoomGenerator {

	public HeightGenZoom(World world, ZoomGenerator parent) {
		super(world, parent);
	}

	@Override
	/*
	 * Zoom in on previous noise. All maps store area off the side, for example a 18x18 map is 16x16, with index 0 and 17 used for smoothing data This is works fine when generating limited size maps, but it doesn't work infinitely
	 * 
	 * Parameters: x position (in blocks), z position, zoom out, size of array to generate (size * size = arraysize)
	 */
	public int[] generate(long x, long z, int size) {
		long px = x >> 1;
		long pz = z >> 1;
		int psize = (size >> 1) + 3;
		int[] pints = parent.generate(px, pz, psize);
		int[] ints = new int[psize * psize * 4];
		int sWidth = psize << 1;
		int sX;

		for (int ax = 0; ax < psize - 1; ++ax) {
			sX = ax << 1;
			int sWidx = sX * sWidth;
			int xp0 = pints[0 + (ax + 0) * psize];
			int xp1 = pints[0 + (ax + 1) * psize];

			for (int az = 0; az < psize - 1; ++az) {
				// initSeed((long)(az + px << 1), (long)(ax + pz << 1));
				int zp0 = pints[az + 1 + (ax + 0) * psize];
				int zp1 = pints[az + 1 + (ax + 1) * psize];
				ints[sWidx] = xp0;
				ints[sWidx++ + sWidth] = mean(xp0, xp1);
				ints[sWidx] = this.mean(xp0, zp0);
				ints[sWidx++ + sWidth] = mean(xp0, zp0, xp1, zp1);
				xp0 = zp0;
				xp1 = zp1;
			}
		}

		int[] out = new int[size * size];

		for (sX = 0; sX < size; ++sX) {
			System.arraycopy(ints, (int) ((sX + (z & 1)) * (psize << 1) + (x & 1)), out, sX * size, size);
		}

		return out;
	}

	protected int mean(int a, int b) {
		return MathUtils.floor((a + b) / 2f);
	}

	protected int mean(int a, int b, int c, int d) {
		return MathUtils.floor((a + b + c + d) / 4f);
	}

}
